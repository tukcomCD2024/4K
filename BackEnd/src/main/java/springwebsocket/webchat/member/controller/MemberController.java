package springwebsocket.webchat.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springwebsocket.webchat.member.dto.request.LoginRequest;
import springwebsocket.webchat.member.dto.request.SignUpRequest;
import springwebsocket.webchat.member.dto.response.UserResponse;
import springwebsocket.webchat.member.dto.response.loginMessage;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.service.MemberService;

import java.util.Optional;

@Tag(name = "Member API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService userService;
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> register(final @RequestBody SignUpRequest request) {
        UserResponse userResponse = userService.signUp(request);
        return ResponseEntity.ok().body(userResponse);
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
    public ResponseEntity<loginMessage> login(@RequestBody LoginRequest request) {
        log.info("/member/login");
        return userService.login(request.getLoginEmail(), request.getPassword());
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        log.info("/member/logout");
        return userService.logout();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("/member/reissue");
        return userService.reissue(request, response);
    }


}
