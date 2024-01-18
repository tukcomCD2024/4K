package springwebsocket.webchat.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springwebsocket.webchat.dao.User;
import springwebsocket.webchat.dto.UserUpdateDto;

import javax.persistence.EntityManager;
import java.util.Optional;

@Transactional  // Jpa에서 데이터의 변경이 있을 경우 Transactional 을 써야함
@Repository
@Slf4j
public class JpaUserRepository implements UserRepository{

    private EntityManager em;

    public JpaUserRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public void update(Long userId, UserUpdateDto updateParam) {
        User findUser = em.find(User.class, userId);
        findUser.setEmail(updateParam.getEmail());
        findUser.setPassword(updateParam.getPassword());
        findUser.setName(updateParam.getName());
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public void delete(Long id) {
        User user = em.find(User.class,id);
        if (user != null) {
            em.remove(user);
        }
    }
}