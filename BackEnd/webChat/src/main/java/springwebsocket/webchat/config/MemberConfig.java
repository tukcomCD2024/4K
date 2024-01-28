package springwebsocket.webchat.config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springwebsocket.webchat.repository.JpaMemberRepository;
import springwebsocket.webchat.repository.MemberRepository;
import springwebsocket.webchat.service.login.loginService;
import springwebsocket.webchat.service.user.MemberService;
import springwebsocket.webchat.service.user.MemberServiceV1;


@Configuration
public class MemberConfig {

    private final EntityManager em;

    public MemberConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberService userService() {
        return new MemberServiceV1(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

    @Bean
    public loginService loginService() {
        return new loginService(memberRepository());
    }
}
