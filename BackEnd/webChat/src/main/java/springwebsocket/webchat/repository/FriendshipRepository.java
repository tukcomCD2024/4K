package springwebsocket.webchat.repository;

import springwebsocket.webchat.entity.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository{

    // 친구 요청 보내기
    Friendship sendFriendRequest(String senderEmail, String receiverEmail);

    // 친구 요청 수락
    void acceptFriendRequestById(String senderEmail, String receiverEmail);

    // 친구 요청 거절
    void rejectFriendRequestById(Long id);

    // 나에게 온 친구 요청  목록 조회
    Optional<Friendship> findByFriendIdAndStatus(Long userId, Friendship.FriendshipStatus status);

    // 내가 보낸 친구 요청 목록 조회
    Optional<Friendship> findByUserIdAndStatus(Long userId, Friendship.FriendshipStatus status);

    // 서로 친구인 친구 목록 조회
    Optional<Friendship> findByUserIdAndStatusOrFriendIdAndStatus(Long userId, Friendship.FriendshipStatus status1, Long friendId, Friendship.FriendshipStatus status2);

}
