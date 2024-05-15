package springwebsocket.webchat.member.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenMessage {
    String AccessToken;
    String RefreshToken;

    public TokenMessage(String accessToken, String refreshToken) {
        AccessToken = accessToken;
        RefreshToken = refreshToken;
    }
}
