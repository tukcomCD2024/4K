package springwebsocket.webchat.rtc.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.repository.springdata.SpringDataJpaMemberRepository;
import springwebsocket.webchat.naver.TranslateResponseDto;
import springwebsocket.webchat.naver.TranslateService;
import springwebsocket.webchat.rtc.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignalHandler extends TextWebSocketHandler {

    private final List<User> users = new ArrayList<>();

    private final TranslateService translateService;

    private final SpringDataJpaMemberRepository memberRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.info("add user Session ={}", session);
        // WebSocket 연결이 수립될 때마다 호출되는 메서드입니다.
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        // WebSocket 연결이 종료될 때 호출되는 메서드입니다.
        // 종료된 세션을 users 리스트에서 제거합니다.
        log.info("delete user Session ={}", session);
        users.removeIf(user -> user.getSession().equals(session));
        log.info("users List ={}", users);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        // JSON 문자열을 파싱하여 데이터를 추출합니다.
        JSONObject data = new JSONObject(payload);
        String type = data.getString("type");

        switch (type) {
            case "store_user":
                log.info("store_user");
                handleStoreUser(session, data);
                break;
            case "start_call":
                log.info("start_call");
                handleStartCall(session, data);
                break;
            case "create_offer":
                log.info("create_offer");
                handleCreateOffer(session, data);
                break;
            case "create_answer":
                log.info("create_answer");
                handleCreateAnswer(session, data);
                break;
            case "ice_candidate":
                log.info("ice_candidate");
                handleIceCandidate(session, data);
                break;
            case "stt_message":
                log.info("stt_message");
                String sttData = data.getString("data"); // data 속성을 추출합니다.
                log.info("STT Data: " + sttData); // 추출한 데이터를 로그에 출력합니다.
                handlestt(session, data);
                break;
            default:
                // 처리되지 않은 유형에 대한 로직을 추가할 수 있습니다.
                break;
        }
    }

    private void handlestt(WebSocketSession session, JSONObject data) throws IOException {
        String name = data.getString("name");
        String target = data.getString("target");

        Optional<User> user = findUser(name);
        Optional<User> targetUser = findUser(target);

        String srcLang = user.get().getLanguage();
        String tarLang = targetUser.get().getLanguage();
        String textData = data.getString("data");

        TranslateResponseDto.Result result = translateService.naverPapagoTranslate(srcLang, tarLang, textData);

        if (targetUser != null) {
            sendMessage(session, "translate_message",result.getTranslatedText(),name,tarLang);
            sendMessage(targetUser.get().getSession(), "translate_message",result.getTranslatedText(),name,tarLang);
        }else {
            sendMessage(session, "translate_message", "user is not exist");
        }

    }

    private void handleStoreUser(WebSocketSession session, JSONObject data) throws IOException {
        String name = data.getString("name");
        log.info("name ={}", name);
        Member member = memberRepository.findByEmail(name).get();
        Optional<User> user = findUser(name);
        if (user.isPresent()) {
            sendMessage(session, "user already exists");
        } else {
            // Add new user
            addUser(name, session, member.getLanguage());
        }
    }


    private void handleStartCall(WebSocketSession session, JSONObject data) throws IOException {
        String target = data.getString("target");
        Optional<User> userToCall = findUser(target);
        if (userToCall != null) {
            sendMessage(session, "call_response", "user is ready for call");
        } else {
            sendMessage(session, "call_response", "user is not online");
        }
    }

    private void handleCreateOffer(WebSocketSession session, JSONObject data) throws IOException {
        String target = data.getString("target");
        Optional<User> userToReceiveOffer = findUser(target);
        if (userToReceiveOffer != null) {
            JSONObject offerData = data.getJSONObject("data");
            sendMessage(userToReceiveOffer.get().getSession(), "offer_received", data.getString("name"), offerData.getString("sdp"));
        }
    }

    private void handleCreateAnswer(WebSocketSession session, JSONObject data) throws IOException {
        String target = data.getString("target");
        Optional<User> userToReceiveAnswer = findUser(target);
        if (userToReceiveAnswer != null) {
            JSONObject answerData = data.getJSONObject("data");
            sendMessage(userToReceiveAnswer.get().getSession(), "answer_received", data.getString("name"), answerData.getString("sdp"));
        }
    }

    private void handleIceCandidate(WebSocketSession session, JSONObject data) throws IOException {
        String target = data.getString("target");
        Optional<User> userToReceiveIceCandidate = findUser(target);
        if (userToReceiveIceCandidate != null) {
            JSONObject iceData = data.getJSONObject("data");
            JSONObject candidateData = new JSONObject();
            candidateData.put("sdpMLineIndex", iceData.getInt("sdpMLineIndex"));
            candidateData.put("sdpMid", iceData.getString("sdpMid"));
            candidateData.put("sdpCandidate", iceData.getString("sdpCandidate"));
            sendMessage(userToReceiveIceCandidate.get().getSession(), "ice_candidate", data.getString("name"), candidateData);
        }
    }


    private Optional<User>  findUser(String username) {
        return users.stream()
                .filter(user -> user.getName() != null && user.getName().equals(username))
                .findFirst();
    }

    private void addUser(String name, WebSocketSession session, String language) {
        users.add(new User(name, session, language));
    }

    private void sendMessage(WebSocketSession session, String type, String data) throws IOException {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("data", data);
        session.sendMessage(new TextMessage(message.toString()));
    }

    private void sendMessage(String tarLang, WebSocketSession session, String translateMessage, String translatedText) throws IOException {
        JSONObject message = new JSONObject();
        message.put("type", translateMessage);
        message.put("data", translatedText);
        message.put("target", tarLang);
        session.sendMessage(new TextMessage(message.toString()));
    }


    private void sendMessage(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }

    private void sendMessage(WebSocketSession session, String messageType, String name, String sdp) throws IOException {
        JSONObject message = new JSONObject();
        message.put("type", messageType);
        message.put("name", name);
        message.put("data", sdp);
        session.sendMessage(new TextMessage(message.toString()));
    }

    private void sendMessage(WebSocketSession session, String messageType, String name, JSONObject candidateData) throws IOException {
        JSONObject message = new JSONObject();
        message.put("type", messageType);
        JSONObject data = new JSONObject();
        data.put("name", name);
        message.put("data", candidateData);
        session.sendMessage(new TextMessage(message.toString()));
    }


    private void sendMessage(WebSocketSession session, String type, String data, String username, String target) throws IOException {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("data", data);
        message.put("target",target);
        session.sendMessage(new TextMessage(message.toString()));
    }
}