package springwebsocket.webchat.friend.service;

import springwebsocket.webchat.friend.entity.Friendship;
import springwebsocket.webchat.friend.repository.springdata.UserInfoMapping;
import springwebsocket.webchat.member.entity.Member;

import java.util.List;

public interface FriendshipService {

    // 친구 요청 보내기
    Friendship sendFriendRequest(String senderEmail, String receiverEmail);

    // 친구 요청 수락
    void acceptFriendRequestById(Long senderId, String receiverEmail);

    // 친구 요청 거절
    void rejectFriendRequestById(Long id, String Email);

    // 나에게 온 친구 요청 목록 조회
    List<UserInfoMapping> findByFriendIdAndStatus(Long id);

    // 서로 친구인 친구 목록 조회
    List<String> findByUserIdAndStatusOrFriendIdAndStatus(Long userId);

}

