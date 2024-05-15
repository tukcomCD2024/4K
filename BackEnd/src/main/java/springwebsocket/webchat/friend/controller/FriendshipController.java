package springwebsocket.webchat.friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springwebsocket.webchat.friend.dto.request.UserEmailRequest;
import springwebsocket.webchat.friend.dto.request.UserRequest;
import springwebsocket.webchat.friend.repository.springdata.UserInfoMapping;
import springwebsocket.webchat.friend.service.FriendshipService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/sendFriendRequest")
    public ResponseEntity<?> sendFriendRequest(@RequestBody UserEmailRequest userEmailRequest) {

        String senderEmail = userEmailRequest.getSenderEmail();
        String receiverEmail = userEmailRequest.getReceiverEmail();

        return friendshipService.sendFriendRequest(senderEmail, receiverEmail);

    }

    @PostMapping("/acceptFriendRequestById")
    public ResponseEntity<?> acceptFriendRequestById(@RequestBody UserEmailRequest userEmailRequest) {
        String senderEmail = userEmailRequest.getSenderEmail();
        String receiverEmail = userEmailRequest.getReceiverEmail();

        return friendshipService.acceptFriendRequestById(senderEmail, receiverEmail);
    }

    @PostMapping("/rejectFriendRequestById")
    public ResponseEntity<?> rejectFriendRequestById(@RequestBody UserEmailRequest userEmailRequest) {
        String senderEmail = userEmailRequest.getSenderEmail();
        String receiverEmail = userEmailRequest.getReceiverEmail();
        return friendshipService.rejectFriendRequestById(senderEmail, receiverEmail);
    }

    @PostMapping("/findByFriendIdAndStatus")
    public List<UserInfoMapping> findByFriendIdAndStatus(@RequestBody UserRequest email) {
        return friendshipService.findByFriendIdAndStatus(email.getEmail());
    }

    @PostMapping("/findByUserIdAndStatusOrFriendIdAndStatus")
    public List<UserInfoMapping> findByUserIdAndStatusOrFriendIdAndStatus(@RequestBody UserRequest email) {
        return friendshipService.findByUserIdAndStatusOrFriendIdAndStatus(email.getEmail());
    }
}
