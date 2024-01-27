package springwebsocket.webchat.repository;

import springwebsocket.webchat.dao.User;
import springwebsocket.webchat.dto.UserUpdateDto;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    void update(Long userId, UserUpdateDto updateParam);

    Optional<User> findById(Long id);

    void delete(Long id);

    Optional<User> findByLoginEmail(String loginEmail);
}
