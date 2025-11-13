import org.example.dao.FriendRequestDAO;
import org.example.dao.UserDAO;
import org.example.model.FriendRequest;
import org.example.service.FriendRequestService;
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
public class FriendRequestServiceTest {
    @Mock
    private FriendRequestDAO reqDao;

    @Mock
    private UserDAO userDao;

    @InjectMocks
    private FriendRequestService service;

    @Test
    void testSendReq() {
        Mockito.when(userDao.getUserIdByUsername("charan")).thenReturn(1);
        Mockito.when(userDao.getUserIdByUsername("likith")).thenReturn(2);
        Mockito.when(reqDao.sendRequest(Mockito.any())).thenReturn(true);

        service.sendFriendRequest("charan", "likith");

        Mockito.verify(reqDao).sendRequest(Mockito.any());
    }

    @Test
    void testViewPending() {
        Mockito.when(userDao.getUserIdByUsername("charan")).thenReturn(1);
        Mockito.when(reqDao.getPendingRequests(1)).thenReturn(Collections.emptyList());

        service.viewPendingRequests("charan");

        Mockito.verify(reqDao).getPendingRequests(1);
    }

    @Test
    void testAcceptRequest() {
        FriendRequest fr = new FriendRequest(2, 1, "PENDING");
        fr.setRequestId(10);

        Mockito.when(userDao.getUserIdByUsername("nithin")).thenReturn(1);
        Mockito.when(reqDao.getRequestById(10)).thenReturn(fr);
        Mockito.when(reqDao.acceptRequest(10)).thenReturn(true);

        service.acceptFriendRequest("nithin", 10);

        Mockito.verify(reqDao).acceptRequest(10);
    }
}
