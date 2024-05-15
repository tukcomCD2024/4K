package springwebsocket.webchat.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import springwebsocket.webchat.member.entity.RefreshMember;

public interface RefreshMemberRepository extends JpaRepository<RefreshMember, Long> {

    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);
}
