package springwebsocket.webchat.member.service;

import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.dto.request.SignUpRequest;
import springwebsocket.webchat.member.dto.response.UserResponse;
import springwebsocket.webchat.member.entity.Member;

import java.util.Optional;

public interface MemberServiceV3 {
    UserResponse signUp(SignUpRequest signUpRequest);

    void update(Long userId, MemberUpdataDto updateParam);

    Optional<Member> findById(Long id);

    void delete(Long id);
}
