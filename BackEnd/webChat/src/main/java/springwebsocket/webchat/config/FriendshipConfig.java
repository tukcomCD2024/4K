package springwebsocket.webchat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springwebsocket.webchat.repository.FriendshipRepository;
import springwebsocket.webchat.repository.JpaFriendshipRepository;
import springwebsocket.webchat.repository.springdata.SpringDataJpaFriendshipRepository;
import springwebsocket.webchat.repository.springdata.SpringDataJpaMemberRepository;
import springwebsocket.webchat.service.friend.FriendshipService;
import springwebsocket.webchat.service.friend.FriendshipServiceV1;

@Configuration
@RequiredArgsConstructor
public class FriendshipConfig {

    private final SpringDataJpaFriendshipRepository springDataJpaFriendshipRepository;
    private final SpringDataJpaMemberRepository springDataJpaMemberRepository;

    @Bean
    public FriendshipService friendshipService() {
        return new FriendshipServiceV1(friendshipRepository());
    }

    @Bean
    public FriendshipRepository friendshipRepository() {
        return new JpaFriendshipRepository(springDataJpaFriendshipRepository,springDataJpaMemberRepository);
    }
}
