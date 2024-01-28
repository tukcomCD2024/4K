package springwebsocket.webchat.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springwebsocket.webchat.entity.Friendship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Slf4j
@SpringBootTest
class JpaFriendshipRepositoryTest {

    @Autowired
    private JpaFriendshipRepository friendshipRepository;

    @Test
    void sendFriendRequest() {
        // Given
        Long senderId = 1L;
        Long receiverId = 2L;

        // When
        Friendship savedFriendship = friendshipRepository.sendFriendRequest(senderId, receiverId);

        // Then
        assertNotNull(savedFriendship.getId());
        assertEquals(senderId, savedFriendship.getUser().getId());
        assertEquals(receiverId, savedFriendship.getFriend().getId());
        assertEquals(Friendship.FriendshipStatus.PENDING, savedFriendship.getStatus());

    }
}