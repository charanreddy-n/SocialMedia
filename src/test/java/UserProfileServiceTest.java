import org.example.dao.UserDAO;
import org.example.dao.UserProfileDAO;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {
    @Mock
    UserDAO userDao;

    @Mock
    UserProfileDAO profileDao;

    @InjectMocks
    UserService service;

    @Test
    void testProfileCreate() {
        Mockito.when(userDao.getUserIdByUsername("aa")).thenReturn(10);
        Mockito.when(profileDao.addProfile(Mockito.any())).thenReturn(true);

        service.createProfile("aa","bio","blr");

        Assertions.assertEquals(10, userDao.getUserIdByUsername("aa"));
        Mockito.verify(profileDao).addProfile(Mockito.any());
        Assertions.assertTrue(profileDao.addProfile(Mockito.any()));
    }

    @Test
    void testProfileUpdate() {
        Mockito.when(userDao.getUserIdByUsername("aa")).thenReturn(10);
        Mockito.when(profileDao.updateProfile(Mockito.any())).thenReturn(true);

        service.updateProfile("aa","new","hyd");

        Assertions.assertEquals(10, userDao.getUserIdByUsername("aa"));
        Mockito.verify(profileDao).updateProfile(Mockito.any());
        Assertions.assertTrue(profileDao.updateProfile(Mockito.any()));
    }
}
