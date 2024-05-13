package springwebsocket.webchat.friend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.friend.dto.request.UserRequest;
import springwebsocket.webchat.friend.entity.Friendship;
import springwebsocket.webchat.friend.repository.springdata.UserInfoMapping;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.friend.repository.FriendshipRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipServiceV1 implements FriendshipService{

    private final FriendshipRepository friendshipRepository;

    @Override
    public ResponseEntity<?> sendFriendRequest(String senderEmail, String receiverEmail) {
        return friendshipRepository.sendFriendRequest(senderEmail, receiverEmail);
    }

    @Override
    public ResponseEntity<?> acceptFriendRequestById(String senderEmail, String receiverEmail) {
        return friendshipRepository.acceptFriendRequestById(senderEmail, receiverEmail);
    }

    @Override
    public ResponseEntity<?> rejectFriendRequestById(String senderEmail, String receiverEmail) {
        return friendshipRepository.rejectFriendRequestById(senderEmail, receiverEmail);
    }

    @Override
    public List<UserInfoMapping> findByFriendIdAndStatus(String email) {
        return friendshipRepository.findByFriendIdAndStatus(email);
    }

    @Override
    public List<UserInfoMapping> findByUserIdAndStatusOrFriendIdAndStatus(Long userId) {
        return friendshipRepository.findByUserIdAndStatusOrFriendIdAndStatus(userId);
    }
}
