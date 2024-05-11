//package springwebsocket.webchat.fcm.controller;
//
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import springwebsocket.webchat.fcm.dto.FCMNotificationRequestDto;
//import springwebsocket.webchat.fcm.service.FCMNotificationService;
//
//@Tag(name = "Notification", description = "FCM Notification 관련 api 입니다.")
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/v1/notification")
//public class FCMNotificationController {
//
//    private final FCMNotificationService fcmNotificationService;
//
//    @PostMapping()
//    public String sendNotificationByToken(@RequestBody FCMNotificationRequestDto requestDto) {
//        return fcmNotificationService.sendNotificationByToken(requestDto);
//    }
//
//}
