package springwebsocket.webchat.friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springwebsocket.webchat.friend.dto.request.UserIdRequest;
import springwebsocket.webchat.friend.dto.request.UserEmailRequest;
import springwebsocket.webchat.friend.entity.Friendship;
import springwebsocket.webchat.friend.repository.springdata.UserInfoMapping;
import springwebsocket.webchat.friend.service.FriendshipService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/sendFriendRequest")
    public String sendFriendRequest(@RequestBody UserEmailRequest userEmailRequest) {

        String senderEmail = userEmailRequest.getSenderEmail();
        String receiverEmail = userEmailRequest.getReceiverEmail();

        Friendship friendship = friendshipService.sendFriendRequest(senderEmail, receiverEmail);

        if (friendship == null) {
            return "fail";
        } else {
            return "success";
        }
    }

    @PostMapping("/acceptFriendRequestById")
    public String acceptFriendRequestById(Long senderId, String receiverEmail) {
        friendshipService.acceptFriendRequestById(senderId, receiverEmail);

        return "success";
    }

    @PostMapping("/rejectFriendRequestById")
    public String rejectFriendRequestById(Long id, String Email) {
        friendshipService.rejectFriendRequestById(id, Email);
        return "success";
    }

    @PostMapping("/findByFriendIdAndStatus")
    public List<UserInfoMapping> findByFriendIdAndStatus(@RequestBody UserIdRequest id) {
        return friendshipService.findByFriendIdAndStatus(id.getUserId());
    }

    @PostMapping("/findByUserIdAndStatusOrFriendIdAndStatus")
    public List<String> findByUserIdAndStatusOrFriendIdAndStatus(@RequestBody UserIdRequest request) {
        return friendshipService.findByUserIdAndStatusOrFriendIdAndStatus(request.getUserId());
    }

    @PostMapping("/findByUserIdAndStatusOrFriendIdAndStatusAny")
    public List<String> findByUserIdAndStatusOrFriendIdAndStatusAny(Long userId) {
        return friendshipService.findByUserIdAndStatusOrFriendIdAndStatus(userId);
    }
}
