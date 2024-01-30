package springwebsocket.webchat.service.user;

import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.dto.UserUpdateDto;

import java.util.Optional;

public interface MemberService {

    Member save(Member user);

    void update(Long userId, UserUpdateDto updateParam);

    Optional<Member> findById(Long id);

    void delete(Long id);

}
