package springwebsocket.webchat.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.dto.request.SignUpRequest;
import springwebsocket.webchat.member.dto.response.UserResponse;
import springwebsocket.webchat.member.entity.Member;

import java.util.Optional;

public interface MemberService {
    UserResponse signUp(SignUpRequest signUpRequest);

    void update(Long userId, MemberUpdataDto updateParam);

    Optional<Member> findById(Long id);

    void delete(Long id);

    ResponseEntity<String> login(String loginEmail, String password);

    ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response);
}
