package springwebsocket.webchat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springwebsocket.webchat.dto.FCMNotificationRequestDto;
import springwebsocket.webchat.service.fcm.FCMNotificationService;

@Tag(name = "Notification", description = "FCM Notification 관련 api 입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notification")
public class FCMNotificationController {

    private final FCMNotificationService fcmNotificationService;

    @PostMapping()
    public String sendNotificationByToken(@RequestBody FCMNotificationRequestDto requestDto) {
        return fcmNotificationService.sendNotificationByToken(requestDto);
    }

}
