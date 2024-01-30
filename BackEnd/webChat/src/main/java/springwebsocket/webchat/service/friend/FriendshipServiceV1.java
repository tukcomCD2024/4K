package springwebsocket.webchat.service.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.repository.FriendshipRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipServiceV1 implements FriendshipService{

    private final FriendshipRepository friendshipRepository;

    @Override
    public Friendship sendFriendRequest(Long senderId, String receiverEmail) {
        return friendshipRepository.sendFriendRequest(senderId, receiverEmail);
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
    public List<Long> findByUserIdAndStatusOrFriendIdAndStatus(Long userId) {
        return friendshipRepository.findByUserIdAndStatusOrFriendIdAndStatus(userId);
    }
}
