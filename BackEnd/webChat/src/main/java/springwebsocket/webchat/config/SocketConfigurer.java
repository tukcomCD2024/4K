package springwebsocket.webchat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import springwebsocket.webchat.handler.SocketHandler;


// webRTC 예제 config
@Configuration
@EnableWebSocket
public class SocketConfigurer implements WebSocketConfigurer {
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new SocketHandler(), "/socket").setAllowedOrigins("*");
	}
}
