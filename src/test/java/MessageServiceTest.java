import org.example.dao.MessageDAO;
import org.example.dao.UserDAO;
import org.example.model.Message;
import org.example.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @Mock
    private MessageDAO msgDao;

    @Mock
    private UserDAO userDao;

    @InjectMocks
    private MessageService service;

    @Test
    void sendMsg_ok() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(2);

        Mockito.when(msgDao.send(Mockito.any(Message.class))).thenReturn(true);

        service.sendMsg("a", "b", "hi");
        Mockito.verify(msgDao).send(Mockito.any(Message.class));
    }


    @Test
    void sendMsg_userNotFound() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(-1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(2);

        service.sendMsg("a", "b", "test");

        Mockito.verify(msgDao, Mockito.never()).send(Mockito.any());
    }

    @Test
    void showChat_ok() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(2);

        Message m1 = new Message(1, 2, "hi");
        m1.setMsgId(10);

        Mockito.when(msgDao.getConversation(1, 2)).thenReturn(List.of(m1));
        Mockito.when(userDao.getUsernameById(1)).thenReturn("a");
        service.showChat("a", "b");

        Mockito.verify(msgDao).getConversation(1, 2);
    }


    @Test
    void showChat_userNotFound() {
        Mockito.when(userDao.getUserIdByUsername("a")).thenReturn(-1);
        Mockito.when(userDao.getUserIdByUsername("b")).thenReturn(2);

        service.showChat("a", "b");

        Mockito.verify(msgDao, Mockito.never())
                .getConversation(Mockito.anyInt(), Mockito.anyInt());
    }

}
