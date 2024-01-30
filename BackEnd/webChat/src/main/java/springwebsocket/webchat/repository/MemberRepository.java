package springwebsocket.webchat.repository;

import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.dto.UserUpdateDto;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member user);

    void update(Long userId, UserUpdateDto updateParam);

    Optional<Member> findById(Long id);

    void delete(Long id);

    Optional<Member> findByLoginEmail(String loginEmail);
}
