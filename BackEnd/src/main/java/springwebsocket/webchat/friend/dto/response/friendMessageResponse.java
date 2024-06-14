package springwebsocket.webchat.friend.dto.response;

import lombok.Getter;

@Getter
public class friendMessageResponse {
    String message;

    public friendMessageResponse(String message) {
        this.message = message;
    }
}
