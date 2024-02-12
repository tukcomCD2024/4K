package springwebsocket.webchat.rtc.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        // WebSocket 연결이 수립될 때마다 호출되는 메서드입니다.
        // 클라이언트에서 전달된 이름을 가져옵니다.
        String name = (String) session.getAttributes().get("name");
        // 새로운 사용자 세션을 users 리스트에 추가합니다.
        users.add(new User(name, session));
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        // WebSocket 연결이 종료될 때 호출되는 메서드입니다.
        // 종료된 세션을 users 리스트에서 제거합니다.
        users.removeIf(user -> user.getSession().equals(session));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        String payload = message.getPayload();
        // JSON 문자열을 파싱하여 데이터를 추출합니다.
        JSONObject data = new JSONObject(payload);
        String type = data.getString("type");

        switch (type) {
            case "store_user":
                log.info("store_user");
                handleStoreUser(session, data);
                log.info("user ={}",users.get(1).getName());
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
            default:
                // 처리되지 않은 유형에 대한 로직을 추가할 수 있습니다.
                break;
        }
    }

    private void handleStoreUser(WebSocketSession session, JSONObject data) throws IOException {
        String name = data.getString("name");
        Optional<User> user = findUser(name);
        if (user != null) {
            // User already exists
            log.info("user != null");
            sendMessage(session, "user already exists");
        } else {
            // Add new user
            log.info("user == null");
            addUser(name, session);
            for (User u : users) {
                log.info("User: {}", u.getName());
            }
        }
    }

    private void handleStartCall(WebSocketSession session, JSONObject data) throws IOException {
        String target = data.getString("target");
        Optional<User> userToCall = findUser(target);
        if (userToCall != null) {
            sendMessage(userToCall.get().getSession(), "call_response", "user is ready for call");
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


    private Optional<User> findUser(String username) {
        return users.stream()
                .filter(user -> user.getName() != null && user.getName().equals(username))
                .findFirst();
    }

    private void addUser(String name, WebSocketSession session) {
        users.add(new User(name, session));
    }

    private void sendMessage(WebSocketSession session, String type, String data) throws IOException {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("data", data);
        session.sendMessage(new TextMessage(message.toString()));
    }

    private void sendMessage(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }
    private void sendMessage(WebSocketSession session, String messageType, String name, String sdp) throws IOException {
        JSONObject message = new JSONObject();
        message.put("type", messageType);
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("data", new JSONObject().put("sdp", sdp));
        message.put("data", data);
        session.sendMessage(new TextMessage(message.toString()));
    }

    private void sendMessage(WebSocketSession session, String messageType, String name, JSONObject candidateData) throws IOException {
        JSONObject message = new JSONObject();
        message.put("type", messageType);
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("data", candidateData);
        message.put("data", data);
        session.sendMessage(new TextMessage(message.toString()));
    }

}
