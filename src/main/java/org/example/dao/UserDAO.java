package org.example.dao;

import org.example.model.User;
import org.example.model.UserProfile;

import java.util.List;

public interface UserDAO {

    boolean registerUser(org.example.model.User u);

    boolean loginUser(String username, String password);

    UserProfile viewProfile(String username);

    int getUserIdByUsername(String username);

    String getUsernameById(int id);

    boolean generateAndStoreOTP(String username);

    boolean verifyOTP(String username, String otp);

    boolean resetPassword(String username, String newPass);

    List<String> getFollowingUsernames(String username);

}
