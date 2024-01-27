package springwebsocket.webchat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import springwebsocket.webchat.dao.ChatDAO;
import springwebsocket.webchat.dto.ChatRoom;
import springwebsocket.webchat.service.chat.ChatService;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;

    private final ChatService service;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        ChatDAO chatMessage = mapper.readValue(payload, ChatDAO.class);
        log.info("session {}", payload);

        ChatRoom room = service.findRoomById(chatMessage.getRoomId());
        log.info("room {}", room.toString());

        room.handleAction(session, chatMessage, service);

    }
}
