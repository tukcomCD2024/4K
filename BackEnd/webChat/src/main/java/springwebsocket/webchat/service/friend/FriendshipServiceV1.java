package springwebsocket.webchat.service.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.repository.FriendshipRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipServiceV1 implements FriendshipService{

    private final FriendshipRepository friendshipRepository;


    @Override
    public void sendFriendRequest(Long userId, Long friendId) {

    }

    @Override
    public void acceptFriendRequest(Long userId, Long friendId) {

    }

    @Override
    public void rejectFriendRequest(Long userId, Long friendId) {

    }

    @Override
    public List<Friendship> getIncomingFriendRequests(Long userId) {
        return null;
    }

    @Override
    public List<Friendship> getOutgoingFriendRequests(Long userId) {
        return null;
    }

    @Override
    public List<Friendship> getMutualFriends(Long userId) {
        return null;
    }
}
