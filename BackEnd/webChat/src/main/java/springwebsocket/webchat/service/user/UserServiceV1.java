package springwebsocket.webchat.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.dao.User;
import springwebsocket.webchat.dto.UserUpdateDto;
import springwebsocket.webchat.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceV1 implements UserService{

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void update(Long userId, UserUpdateDto updateParam) {
        userRepository.update(userId, updateParam);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }
}
