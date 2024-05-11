package springwebsocket.webchat.member.dto.response;


import lombok.Getter;

@Getter
public class loginMessage {

    String message;

    public loginMessage(String message) {
        this.message = message;
    }
}
