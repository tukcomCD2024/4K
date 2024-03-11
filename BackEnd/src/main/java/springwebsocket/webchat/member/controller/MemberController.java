package springwebsocket.webchat.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springwebsocket.webchat.member.dto.request.LoginRequest;
import springwebsocket.webchat.member.dto.request.SignUpRequest;
import springwebsocket.webchat.member.dto.response.UserResponse;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.exception.EmailDuplicatedException;
import springwebsocket.webchat.member.service.MemberServiceV1;
import springwebsocket.webchat.member.service.MemberServiceV2;
import springwebsocket.webchat.member.service.MemberServiceV3;

import java.util.Optional;

@Tag(name = "Member API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberServiceV3 userService;
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody  final SignUpRequest request) {
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
    public String login(@RequestBody LoginRequest request) {
        return userService.login(request.getLoginEmail(), request.getPassword());
    }
}
