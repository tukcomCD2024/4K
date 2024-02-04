package springwebsocket.webchat.rtc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import springwebsocket.webchat.rtc.handler.ExampleRtcSocketHandler;

@Configuration
@EnableWebSocket
public class ExampleRtcConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ExampleRtcSocketHandler(), "/socket")
                .setAllowedOrigins("*");
    }
}
