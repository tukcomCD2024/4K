package springwebsocket.webchat.repository.springdata;

import org.springframework.data.jpa.repository.JpaRepository;
import springwebsocket.webchat.entity.Friendship;

public interface SpringDataJpaFriendshipRepository extends JpaRepository<Friendship,Long> {

}
