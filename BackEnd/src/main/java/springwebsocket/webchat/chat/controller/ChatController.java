package springwebsocket.webchat.chat.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springwebsocket.webchat.chat.dto.ChatRoom;
import springwebsocket.webchat.chat.service.ChatService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService service;


    @PostMapping
    public ChatRoom creatRoom() {
        return service.createRoom();
    }

    @GetMapping
    public List<ChatRoom> findAllRooms() {
        return service.findAllRoom();
    }

    @PostMapping("/delete")
    public String deleteRoom(@RequestParam String roomId) {
        return service.delete(roomId);
    }
}
