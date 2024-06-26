package springwebsocket.webchat.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springwebsocket.webchat.global.response.ApiResponse;
import springwebsocket.webchat.member.dto.request.EmailRequest;
import springwebsocket.webchat.member.dto.request.EmailPasswordRequest;
import springwebsocket.webchat.member.dto.request.LoginRequest;
import springwebsocket.webchat.member.dto.request.SignUpRequest;
import springwebsocket.webchat.member.dto.response.TokenMessage;
import springwebsocket.webchat.member.dto.response.UserResponse;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.service.MemberService;

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
    public ApiResponse<?> update(@RequestBody MemberUpdataDto updateParam) {
        userService.update(updateParam);
        return ApiResponse.createSuccessWithNoContent();
    }

    @PostMapping("/find")
    public ApiResponse<Member> findById(@RequestBody EmailRequest email) {
        Member member = userService.findById(email);
        return ApiResponse.createSuccess(member);
    }

    @PostMapping("/findtarget")
    public ApiResponse<UserResponse> findByTarget(@RequestBody EmailRequest email){
        UserResponse target = userService.findByTarget(email);

        return ApiResponse.createSuccess(target);
    }

    @PostMapping("/delete")
    public ApiResponse<?> delete(@RequestBody EmailPasswordRequest request) {
        userService.delete(request);
        return ApiResponse.createSuccessWithNoContent();
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request) {
        TokenMessage message = userService.login(request);
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
