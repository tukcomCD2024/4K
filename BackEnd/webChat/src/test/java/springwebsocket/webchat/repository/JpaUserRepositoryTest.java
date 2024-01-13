package springwebsocket.webchat.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springwebsocket.webchat.dao.User;
import springwebsocket.webchat.dto.UserUpdateDto;

import static org.assertj.core.api.Assertions.*;


@Slf4j
@SpringBootTest
class JpaUserRepositoryTest {


    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        //given
        User user = new User("asdf@naver.com", "1234", "강지석");

        //when
        User saveUser = userRepository.save(user);


        //then
        User findUser = userRepository.findById(user.getId()).get();
        assertThat(findUser).isEqualTo(saveUser);
    }

    @Test
    void updateUser() {
        //given
        User user = new User("user1@naver.com", "1234", "user1");
        User saveUser = userRepository.save(user);
        Long userId = saveUser.getId();

        //when
        UserUpdateDto updateParam = new UserUpdateDto("user2@naver.com", "1234", "user2");
        userRepository.update(userId, updateParam);

        //then
        User findUser = userRepository.findById(userId).get();
        assertThat(findUser.getEmail()).isEqualTo(updateParam.getEmail());
        assertThat(findUser.getPassword()).isEqualTo(updateParam.getPassword());
        assertThat(findUser.getName()).isEqualTo(updateParam.getName());
    }

}