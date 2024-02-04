package springwebsocket.webchat.rtc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/webRTC")
@Controller
@RequiredArgsConstructor
public class webRTCController {

    @GetMapping
    public String rtcRoom() {
        return "webRTC";
    }

}
