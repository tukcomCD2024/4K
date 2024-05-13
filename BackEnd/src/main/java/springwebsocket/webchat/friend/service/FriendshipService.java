package springwebsocket.webchat.friend.service;

import org.springframework.http.ResponseEntity;
import springwebsocket.webchat.friend.dto.request.UserRequest;
import springwebsocket.webchat.friend.entity.Friendship;
import springwebsocket.webchat.friend.repository.springdata.UserInfoMapping;
import springwebsocket.webchat.member.entity.Member;

import java.util.List;

public interface FriendshipService {

    // 친구 요청 보내기
    ResponseEntity<?> sendFriendRequest(String senderEmail, String receiverEmail);

    // 친구 요청 수락
    ResponseEntity<?> acceptFriendRequestById(String senderEmail, String receiverEmail);

    // 친구 요청 거절
    ResponseEntity<?> rejectFriendRequestById(String senderEmail, String receiverEmail);

    // 나에게 온 친구 요청 목록 조회
    List<UserInfoMapping> findByFriendIdAndStatus(String email);

    // 서로 친구인 친구 목록 조회
    List<UserInfoMapping> findByUserIdAndStatusOrFriendIdAndStatus(String email);

}

