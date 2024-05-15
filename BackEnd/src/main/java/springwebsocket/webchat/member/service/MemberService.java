package springwebsocket.webchat.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import springwebsocket.webchat.global.response.ApiResponse;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.dto.request.EmailRequest;
import springwebsocket.webchat.member.dto.request.SignUpRequest;
import springwebsocket.webchat.member.dto.response.TokenMessage;
import springwebsocket.webchat.member.dto.response.UserResponse;
import springwebsocket.webchat.member.entity.Member;

import java.util.Optional;

public interface MemberService {
    UserResponse signUp(SignUpRequest signUpRequest);

    void update(Long userId, MemberUpdataDto updateParam);

    Optional<Member> findById(Long id);

    UserResponse findByTarget(EmailRequest email);

    void delete(Long id);

    TokenMessage login(String loginEmail, String password);

    ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> logout();
}
