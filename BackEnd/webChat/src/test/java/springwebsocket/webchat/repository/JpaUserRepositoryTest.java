package springwebsocket.webchat.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springwebsocket.webchat.dao.User;


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
        Assertions.assertThat(findUser).isEqualTo(saveUser);
    }

}