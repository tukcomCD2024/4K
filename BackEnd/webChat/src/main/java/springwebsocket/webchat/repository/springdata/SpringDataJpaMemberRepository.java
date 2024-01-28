package springwebsocket.webchat.repository.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import springwebsocket.webchat.entity.Member;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long> {

}
