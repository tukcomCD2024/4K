package springwebsocket.webchat.service.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipServiceV1 implements FriendshipService{


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
