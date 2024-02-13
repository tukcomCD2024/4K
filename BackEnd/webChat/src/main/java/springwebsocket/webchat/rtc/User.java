package springwebsocket.webchat.rtc;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;


public class User {
    private final String name;
    private final WebSocketSession session;

    public User(String name, WebSocketSession session) {
        this.name = name;
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public WebSocketSession getSession() {
        return session;
    }
}
