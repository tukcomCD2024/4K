package springwebsocket.webchat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springwebsocket.webchat.service.friend.FriendshipService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/friendship")
public class FriendshipController {

    private final FriendshipService service;

    @PostMapping
    public void sendFriendRequest(Long senderId, String receiverEmail){

    }
}
