package springwebsocket.webchat.global.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@Slf4j
public class AdminController {

    @GetMapping("/admin")
    public String adminP() {

        log.info("admin ");
        return "admin Controller";
    }
}
