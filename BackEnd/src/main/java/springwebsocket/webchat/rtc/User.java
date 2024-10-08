package springwebsocket.webchat.rtc;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;


@Getter
@Setter
public class User {
    private final String name;

    private final String language;

    private final WebSocketSession session;

    public User(String name, WebSocketSession session,String language) {
        this.name = name;
        this.session = session;
        this.language = language;
    }
}
