package springwebsocket.webchat.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailRequest {
    String email;

    public EmailRequest(String email) {
        this.email = email;
    }

}
