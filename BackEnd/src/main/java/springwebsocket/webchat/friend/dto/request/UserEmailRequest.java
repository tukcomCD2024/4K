package springwebsocket.webchat.friend.dto.request;


import lombok.Getter;

@Getter
public class UserEmailRequest {
    private String senderEmail;
    private String receiverEmail;
}
