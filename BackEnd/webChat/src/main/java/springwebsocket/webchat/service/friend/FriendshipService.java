package springwebsocket.webchat.service.friend;

import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;

import java.util.List;
import java.util.Optional;

public interface FriendshipService {

    // 친구 요청 보내기
    void sendFriendRequest(Long userId, Long friendId);

    // 친구 요청 수락
    void acceptFriendRequest(Long userId, Long friendId);

    // 친구 요청 거절
    void rejectFriendRequest(Long userId, Long friendId);

    // 나에게 온 친구 요청 목록 조회
    List<Friendship> getIncomingFriendRequests(Long userId);

    // 내가 보낸 친구 요청 목록 조회
    List<Friendship> getOutgoingFriendRequests(Long userId);

    // 서로 친구인 친구 목록 조회
    List<Friendship> getMutualFriends(Long userId);

}

