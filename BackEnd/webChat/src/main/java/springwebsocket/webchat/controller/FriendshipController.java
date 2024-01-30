package springwebsocket.webchat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springwebsocket.webchat.entity.Friendship;
import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.service.friend.FriendshipService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/sendFriendRequest")
    public String sendFriendRequest(Long senderId, String receiverEmail) {

        Friendship friendship = friendshipService.sendFriendRequest(senderId, receiverEmail);

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
    public List<Member> findByFriendIdAndStatus(Long id) {
        return friendshipService.findByFriendIdAndStatus(id);
    }

    @PostMapping("/findByUserIdAndStatusOrFriendIdAndStatus")
    public List<Long> findByUserIdAndStatusOrFriendIdAndStatus(Long userId) {
        return friendshipService.findByUserIdAndStatusOrFriendIdAndStatus(userId);
    }
}
