package springwebsocket.webchat.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.dto.request.EmailRequest;
import springwebsocket.webchat.member.dto.request.EmailPasswordRequest;
import springwebsocket.webchat.member.dto.request.SignUpRequest;
import springwebsocket.webchat.member.dto.response.TokenMessage;
import springwebsocket.webchat.member.dto.response.UserResponse;
import springwebsocket.webchat.member.entity.Member;

public interface MemberService {
    UserResponse signUp(SignUpRequest signUpRequest);

    void update(MemberUpdataDto updateParam);

    Member findById(EmailRequest emailRequest);

    UserResponse findByTarget(EmailRequest email);

    void delete(EmailPasswordRequest request);

    TokenMessage login(String loginEmail, String password);

    ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> logout();
}
