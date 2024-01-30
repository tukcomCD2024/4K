package springwebsocket.webchat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.dto.UserUpdateDto;
import springwebsocket.webchat.service.login.loginService;
import springwebsocket.webchat.service.user.MemberServiceV1;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberServiceV1 userService;
    private final loginService loginService;

    @PostMapping("/save")
    public Member save(Member user) {
        return userService.save(user);
    }

    @PostMapping("/update")
    public void update(Long userId, UserUpdateDto updateParam) {
        userService.update(userId, updateParam);
    }

    @PostMapping("/find")
    public Optional<Member> findById(Long id) {
        return userService.findById(id);
    }

    @PostMapping("/delete")
    public void delete(Long id) {
        userService.delete(id);
    }

    @PostMapping("/login")
    public String login(String loginEmail, String password) {
        return loginService.login(loginEmail, password);
    }
}
