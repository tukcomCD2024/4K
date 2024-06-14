package springwebsocket.webchat.member.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springwebsocket.webchat.member.entity.Member;
import springwebsocket.webchat.member.dto.MemberUpdataDto;
import springwebsocket.webchat.member.repository.springdata.SpringDataJpaMemberRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional  // Jpa에서 데이터의 변경이 있을 경우 Transactional 을 써야함
@Repository
@Slf4j
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    private final SpringDataJpaMemberRepository memberRepository ;

    public JpaMemberRepository(EntityManager em,SpringDataJpaMemberRepository memberRepository) {
        this.em = em;
        this.memberRepository = memberRepository;
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

        try {
            return memberRepository.findByEmail(loginEmail);
        } catch (NoSuchElementException e) {
            return null;
        }

        // JPQL을 사용하여 이메일 주소로 사용자를 찾음
//        String jpql = "SELECT u FROM Member u WHERE u.email = :loginEmail";
//        Query query = em.createQuery(jpql, Member.class);
//        query.setParameter("loginEmail", loginEmail);

    }



}
