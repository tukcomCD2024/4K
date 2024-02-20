package springwebsocket.webchat.chat.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;
import springwebsocket.webchat.chat.entity.ChatDAO;
import springwebsocket.webchat.chat.service.ChatService;

import java.util.HashSet;
import java.util.Set;

@Data
public class ChatRoom {
    private String roomId;  //채팅방 아이디
    private String name;    //채팅방 이름
    private Set<WebSocketSession> sessions = new HashSet<>();


    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleAction(WebSocketSession session, ChatDAO message, ChatService service) {
        if (message.getType().equals(ChatDAO.MessageType.ENTER)) {
            // session에 넘어온 세션 추가
            sessions.add(session);

            // message 에는 입장하였다는 메시지를 띄운다
            message.setMessage(message.getSender() + " 님이 입장하셨습니다");
            sendMessage(message, service);
        } else if (message.getType().equals(ChatDAO.MessageType.TALK)) {
            String socMessage = message.getMessage();
            String resultMessage = socMessage + "asdf";
            message.setMessage(resultMessage);
            sendMessage(message, service);
        }
    }

    public <T> void sendMessage(T message, ChatService service) {
        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));
    }
}
