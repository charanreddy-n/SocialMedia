import org.example.dao.UserDAO;
import org.example.dao.UserProfileDAO;
import org.example.model.User;
import org.example.model.UserProfile;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDAO userDao;

    @Mock
    private UserProfileDAO profileDao;

    @InjectMocks
    private UserService service;

    @Test
    void testRegi() {

        Mockito.when(userDao.registerUser(Mockito.any())).thenReturn(true);
        Assertions.assertTrue(service.register("charan", "Charanreddy n", "charanreddyn52@gmail.com", "charan123"));

        Mockito.when(userDao.registerUser(Mockito.any())).thenReturn(false);
        Assertions.assertFalse(service.register("charan", "Charanreddy n", "charanreddyn52@gmail.com", "charan123"));
    }

    @Test
    void testLogin() {
        Mockito.when(userDao.loginUser("charan", "charan123")).thenReturn(true);
        Assertions.assertTrue(service.login("charan", "charan123"));

        Mockito.when(userDao.loginUser("charan", "charan123")).thenReturn(false);
        Assertions.assertFalse(service.login("charan", "charan123"));
    }

    @Test
    void testViewProfile() {
        UserProfile p = new UserProfile(1, "bio", "loc");

        Mockito.when(userDao.viewProfile("charan")).thenReturn(p);
        Assertions.assertDoesNotThrow(() -> service.viewProfile("charan"));
    }

    @Test
    void testCreateProfile() {
        Mockito.when(userDao.getUserIdByUsername("charan")).thenReturn(5);
        Mockito.when(profileDao.addProfile(Mockito.any())).thenReturn(true);

        service.createProfile("charan", "Never ever let yourself down", "Chennai");
        Mockito.verify(profileDao).addProfile(Mockito.any());
    }

    @Test
    void testUpdateProfile() {
        Mockito.when(userDao.getUserIdByUsername("charan")).thenReturn(5);
        Mockito.when(profileDao.updateProfile(Mockito.any())).thenReturn(true);

        service.updateProfile("charan", "Hardtimes teaches you the good lessons", "Chittoor");
        Mockito.verify(profileDao).updateProfile(Mockito.any());
    }

    @Test
    void testForgotPassword_OTPSent() {
        Mockito.when(userDao.generateAndStoreOTP("aa")).thenReturn(true);
        Mockito.when(userDao.verifyOTP("aa","1111")).thenReturn(true);
        Mockito.when(userDao.resetPassword("aa","new")).thenReturn(true);

        boolean otpGen = userDao.generateAndStoreOTP("aa");
        boolean otpOk = userDao.verifyOTP("aa","1111");
        boolean reset = userDao.resetPassword("aa","new");

        Assertions.assertTrue(otpGen);
        Assertions.assertTrue(otpOk);
        Assertions.assertTrue(reset);
    }
}
