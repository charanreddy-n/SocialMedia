import org.example.dao.FriendRequestDAO;
import org.example.dao.UserDAO;
import org.example.model.FriendRequest;
import org.example.service.FriendRequestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FriendReqSerTest {
    @Mock
    private FriendRequestDAO reqDao;

    @Mock
    private UserDAO userDao;

    @InjectMocks
    private FriendRequestService service;


    @Test
    void testSendRequest_UserNotFound() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(-1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(5);

        service.sendFriendRequest("a", "b");

        Assertions.assertEquals(-1, userDao.getUserIdByUsername("a"));
        Assertions.assertNotEquals(-1, userDao.getUserIdByUsername("b"));
        Mockito.verify(reqDao, Mockito.never()).sendRequest(Mockito.any());
    }

    @Test
    void testSendRequest_SelfRequest() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(10);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(10);

        service.sendFriendRequest("a", "b");

        Assertions.assertEquals(10, userDao.getUserIdByUsername("a"));
        Assertions.assertEquals(10, userDao.getUserIdByUsername("b"));
        Mockito.verify(reqDao, Mockito.never()).sendRequest(Mockito.any());
    }

    @Test
    void testSendRequest_DBFailure() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(2);

        Mockito.when(reqDao.sendRequest(Mockito.any())).thenReturn(false);

        service.sendFriendRequest("a", "b");

        Assertions.assertEquals(1, userDao.getUserIdByUsername("a"));
        Assertions.assertEquals(2, userDao.getUserIdByUsername("b"));
        Mockito.verify(reqDao).sendRequest(Mockito.any());
    }

    @Test
    void testAcceptRequest_NoRequestFound() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(10);
        Mockito.when(reqDao.getRequestById(5)).thenReturn(null);

        service.acceptFriendRequest("a", 5);

        Assertions.assertNull(reqDao.getRequestById(5));
        Assertions.assertEquals(10, userDao.getUserIdByUsername("a"));
        Mockito.verify(reqDao, Mockito.never()).acceptRequest(5);
    }

    @Test
    void testAcceptRequest_NotReceiver() {
        FriendRequest req = new FriendRequest(3, 4, "PENDING");
        req.setRequestId(5);

        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(10);
        Mockito.when(reqDao.getRequestById(5)).thenReturn(req);

        service.acceptFriendRequest("a", 5);

        Assertions.assertNotNull(reqDao.getRequestById(5));
        Mockito.verify(reqDao, Mockito.never()).acceptRequest(5);
    }

    @Test
    void testAcceptRequest_DBSuccess() {
        FriendRequest req = new FriendRequest(1, 10, "PENDING");
        req.setRequestId(22);

        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(10);
        Mockito.when(reqDao.getRequestById(22)).thenReturn(req);
        Mockito.when(reqDao.acceptRequest(22)).thenReturn(true);

        service.acceptFriendRequest("a", 22);

        Assertions.assertEquals(10, req.getReceiverId());
        Assertions.assertEquals(1, req.getSenderId());
        Mockito.verify(reqDao).acceptRequest(22);
    }
}
