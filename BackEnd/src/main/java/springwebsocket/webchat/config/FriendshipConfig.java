package springwebsocket.webchat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springwebsocket.webchat.friend.repository.FriendshipRepository;
import springwebsocket.webchat.friend.repository.JpaFriendshipRepository;
import springwebsocket.webchat.friend.repository.springdata.SpringDataJpaFriendshipRepository;
import springwebsocket.webchat.member.repository.springdata.SpringDataJpaMemberRepository;
import springwebsocket.webchat.friend.service.FriendshipService;
import springwebsocket.webchat.friend.service.FriendshipServiceV1;

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
