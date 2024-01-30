package springwebsocket.webchat.service.friend;

import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;

import java.util.List;
import java.util.Optional;

public interface FriendshipService {

    // 친구 요청 보내기
    Friendship sendFriendRequest(Long senderId, String receiverEmail);

    // 친구 요청 수락
    void acceptFriendRequestById(Long senderId, String receiverEmail);

    // 친구 요청 거절
    void rejectFriendRequestById(Long id, String Email);

    // 나에게 온 친구 요청 목록 조회
    List<Member> findByFriendIdAndStatus(Long id);

    // 서로 친구인 친구 목록 조회
    List<Long> findByUserIdAndStatusOrFriendIdAndStatus(Long userId);

}

