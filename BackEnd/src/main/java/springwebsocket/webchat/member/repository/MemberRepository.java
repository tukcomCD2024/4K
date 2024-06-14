package springwebsocket.webchat.member.repository;

import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.dto.MemberUpdataDto;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member user);

    void update(Long userId, MemberUpdataDto updateParam);

    Optional<Member> findById(Long id);

    void delete(Long id);

    Optional<Member> findByLoginEmail(String loginEmail);
}
