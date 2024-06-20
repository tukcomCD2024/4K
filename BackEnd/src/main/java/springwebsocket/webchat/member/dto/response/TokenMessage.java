package springwebsocket.webchat.member.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenMessage {
    String AccessToken;
    String RefreshToken;
    String language;

    public TokenMessage(String accessToken, String refreshToken, String language) {
        AccessToken = accessToken;
        RefreshToken = refreshToken;
        this.language = language;
    }
}
