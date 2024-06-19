package springwebsocket.webchat.fcm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMNotificationRequestDto {
    private String targetUserEmail;
    private String title;
    private String body;


    @Builder
    public FCMNotificationRequestDto(String targetUserEmail, String title, String body) {
        this.targetUserEmail = targetUserEmail;
        this.title = title;
        this.body = body;
    }
}
