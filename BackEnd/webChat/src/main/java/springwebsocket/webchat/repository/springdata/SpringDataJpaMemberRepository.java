package springwebsocket.webchat.repository.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import springwebsocket.webchat.entity.Member;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

}
