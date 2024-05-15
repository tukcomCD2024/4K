package springwebsocket.webchat.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springwebsocket.webchat.global.response.ApiResponse;
import springwebsocket.webchat.member.dto.request.LoginRequest;
import springwebsocket.webchat.member.dto.request.SignUpRequest;
import springwebsocket.webchat.member.dto.response.TokenMessage;
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
    public ApiResponse<?> register(final @RequestBody SignUpRequest request) {

        userService.signUp(request);
        return ApiResponse.createSuccessWithNoContent();
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
    public ApiResponse<?> login(@RequestBody LoginRequest request) {
        TokenMessage message = userService.login(request.getLoginEmail(), request.getPassword());
        return ApiResponse.createSuccess(message);
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
