package springwebsocket.webchat.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springwebsocket.webchat.repository.JpaUserRepository;
import springwebsocket.webchat.repository.UserRepository;
import springwebsocket.webchat.service.login.loginService;
import springwebsocket.webchat.service.user.UserService;
import springwebsocket.webchat.service.user.UserServiceV1;


@Configuration
public class UserConfig {

    private final EntityManager em;

    public UserConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public UserService userService() {
        return new UserServiceV1(userRepository());
    }

    @Bean
    public UserRepository userRepository() {
        return new JpaUserRepository(em);
    }

    @Bean
    public loginService loginService() {
        return new loginService(userRepository());
    }
}
