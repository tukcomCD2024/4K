package springwebsocket.webchat.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.friend.entity.Friendship;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.friend.repository.FriendshipRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipServiceV1 implements FriendshipService{

    private final FriendshipRepository friendshipRepository;

    @Override
    public Friendship sendFriendRequest(String senderEmail, String receiverEmail) {
        return friendshipRepository.sendFriendRequest(senderEmail, receiverEmail);
    }

    @Override
    public void acceptFriendRequestById(Long senderId, String receiverEmail) {
        friendshipRepository.acceptFriendRequestById(senderId, receiverEmail);
    }

    @Override
    public void rejectFriendRequestById(Long id, String Email) {
        friendshipRepository.rejectFriendRequestById(id, Email);
    }

    @Override
    public List<Member> findByFriendIdAndStatus(Long id) {
        return friendshipRepository.findByFriendIdAndStatus(id);
    }

    @Override
    public List<String> findByUserIdAndStatusOrFriendIdAndStatus(Long userId) {
        return friendshipRepository.findByUserIdAndStatusOrFriendIdAndStatus(userId);
    }
}
