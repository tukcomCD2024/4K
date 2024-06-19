package springwebsocket.webchat.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.fcm.dto.FCMNotificationRequestDto;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.repository.springdata.SpringDataJpaMemberRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FCMNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final SpringDataJpaMemberRepository jpaMemberRepository;

    public String sendNotificationByToken(FCMNotificationRequestDto requestDto) {
        Optional<Member> member = jpaMemberRepository.findByEmail(requestDto.getTargetUserEmail());
        if (member.isPresent()) {
            if (member.get().getFirebaseToken() != null) {
                Map<String, String> data = new HashMap<>();
                data.put("title", requestDto.getTitle());
                data.put("body", requestDto.getBody());

                Message message = Message.builder()
                        .setToken(member.get().getFirebaseToken())
                        .putAllData(data)
                        .build();

                try {
                    firebaseMessaging.send(message);
                    return "알림을 성공적으로 전송했습니다. targetUserEmail = " + requestDto.getTargetUserEmail();
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                    return "알림 보내기를 실패하였습니다. targetUserEmail = " + requestDto.getTargetUserEmail();
                }
            }else {
                return "서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserEmail = " + requestDto.getTargetUserEmail();
            }
        }else {
            return "해당 유저가 존재하지 않습니다. targetUserEmail = " + requestDto.getTargetUserEmail();
        }
    }
}
