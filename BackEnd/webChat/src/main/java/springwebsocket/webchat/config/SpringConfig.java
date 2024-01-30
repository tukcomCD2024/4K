package springwebsocket.webchat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import springwebsocket.webchat.handler.WebSocketChatHandler;


@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class SpringConfig implements WebSocketConfigurer {

    //WebSocketHandler 에 관한 생성자 추가
    private final WebSocketChatHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");
    }
}
