import org.example.dao.FollowerDAO;
import org.example.dao.UserDAO;
import org.example.model.Follower;
import org.example.service.FollowerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class    FollowerServiceTest {
    @Mock
    private FollowerDAO followerDao;

    @Mock
    private UserDAO userDao;

    @InjectMocks
    private FollowerService service;

    @Test
    void testFollowUser() {

        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(2);

        service.followUser("a", "b");

        Mockito.verify(followerDao).followUser(Mockito.any());
    }

    @Test
    void testUnfollowUser() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(2);

        Mockito.when(followerDao.unfollowUser(1, 2)).thenReturn(true);

        service.unfollowUser("a", "b");

        Mockito.verify(followerDao).unfollowUser(1, 2);
    }
}
