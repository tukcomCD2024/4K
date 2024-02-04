package springwebsocket.webchat.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.service.MemberServiceV1;

import java.util.Optional;

@Tag(name = "Member API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberServiceV1 userService;

    @PostMapping("/save")
    public Member save(Member user) {
        return userService.save(user);
    }

    @PostMapping("/update")
    public void update(Long userId, MemberUpdataDto updateParam) {
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
        return userService.login(loginEmail, password);
    }
}
