package springwebsocket.webchat.service.chat;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import springwebsocket.webchat.dto.ChatRoom;

import java.io.IOException;
import java.util.*;

@Slf4j
@Data
@Service
public class ChatService {

    private final ObjectMapper mapper;

    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    public void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom() {
        String roomId = UUID.randomUUID().toString();   // 방번호를 식별하기 위한 고유 Id 생성

        //Builder 를 이용해서 ChatRoom 을 Building
        ChatRoom room = ChatRoom.builder()
                .roomId(roomId)
                .build();

        chatRooms.put(roomId, room);    // UUID로 생성한 방 id와 room 정보를 Map에 저장
        return room;
    }

    public String delete(String roomId) {
        chatRooms.remove(roomId);
        return "OK";
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
