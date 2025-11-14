import org.example.dao.FollowerDAO;
import org.example.dao.FriendRequestDAO;
import org.example.dao.UserDAO;
import org.example.model.Follower;
import org.example.model.FriendRequest;
import org.example.service.FollowerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowerServiceTest {
    @Mock
    private FollowerDAO followDao;

    @Mock
    private UserDAO userDao;
    @Mock
    private FriendRequestDAO reqDao;

    @InjectMocks
    private FollowerService service;



    @Test
    void testFollowUser_UserNotFound() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(-1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(5);

        service.followUser("a", "b");

        Assertions.assertEquals(-1, userDao.getUserIdByUsername("a"));
        Assertions.assertNotEquals(-1, userDao.getUserIdByUsername("b"));
        Mockito.verify(followDao, Mockito.never()).followUser(Mockito.any());
    }

    @Test
    void testFollowUser_SelfFollow() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(10);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(10);

        service.followUser("a", "b");

        Assertions.assertEquals(10, userDao.getUserIdByUsername("a"));
        Assertions.assertEquals(10, userDao.getUserIdByUsername("b"));
        Mockito.verify(followDao, Mockito.never()).followUser(Mockito.any());
    }

    @Test
    void testFollowUser_Success() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(2);

        service.followUser("a", "b");

        Assertions.assertEquals(1, userDao.getUserIdByUsername("a"));
        Assertions.assertEquals(2, userDao.getUserIdByUsername("b"));
        Mockito.verify(followDao).followUser(Mockito.any());
    }

    // ----------------------------
    // UNFOLLOW USER
    // ----------------------------

    @Test
    void testUnfollowUser_UserNotFound() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(-1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(5);

        service.unfollowUser("a", "b");

        Assertions.assertEquals(-1, userDao.getUserIdByUsername("a"));
        Assertions.assertEquals(5, userDao.getUserIdByUsername("b"));
        Mockito.verify(followDao, Mockito.never()).unfollowUser(1,2);
    }

    @Test
    void testUnfollowUser_NotFollowing() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(2);

        Mockito.when(followDao.unfollowUser(1,2)).thenReturn(false);

        service.unfollowUser("a", "b");

        Assertions.assertEquals(1, userDao.getUserIdByUsername("a"));
        Assertions.assertEquals(2, userDao.getUserIdByUsername("b"));
        Mockito.verify(followDao).unfollowUser(1,2);
    }

    @Test
    void testUnfollowUser_Success() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(2);

        Mockito.when(followDao.unfollowUser(1,2)).thenReturn(true);

        service.unfollowUser("a", "b");

        Assertions.assertEquals(1, userDao.getUserIdByUsername("a"));
        Assertions.assertEquals(2, userDao.getUserIdByUsername("b"));
        Mockito.verify(followDao).unfollowUser(1,2);
    }
}
