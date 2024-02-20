package springwebsocket.webchat.member.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.dto.MemberUpdataDto;

import java.util.Optional;

@Transactional  // Jpa에서 데이터의 변경이 있을 경우 Transactional 을 써야함
@Repository
@Slf4j
public class JpaMemberRepository implements MemberRepository {

    private EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member user) {
        em.persist(user);
        return user;
    }

    @Override
    public void update(Long userId, MemberUpdataDto updateParam) {
        Member findUser = em.find(Member.class, userId);
        findUser.setEmail(updateParam.getEmail());
        findUser.setPassword(updateParam.getPassword());
        findUser.setName(updateParam.getName());
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member user = em.find(Member.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public void delete(Long id) {
        Member user = em.find(Member.class,id);
        if (user != null) {
            em.remove(user);
        }
    }

    @Override
    public Optional<Member> findByLoginEmail(String loginEmail) {
        log.info("findByLoginEmail 왔음");

        // JPQL을 사용하여 이메일 주소로 사용자를 찾음
        String jpql = "SELECT u FROM Member u WHERE u.email = :loginEmail";
        Query query = em.createQuery(jpql, Member.class);
        query.setParameter("loginEmail", loginEmail);

        try {
            Member user = (Member) query.getSingleResult();
            log.info("user = {}", user.getClass());
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            // 결과가 없을 경우 NoResultException이 발생하므로 이를 잡아서 빈 Optional 반환
            return Optional.empty();
        }
    }



}
