package org.example.service;

import org.example.controller.SocialMediaAppMain;
import org.example.dao.UserDAO;
import org.example.dao.UserDAOImpl;
import org.example.dao.UserProfileDAO;
import org.example.dao.UserProfileDAOImpl;
import org.example.model.User;
import org.example.model.UserProfile;

import java.util.Scanner;

public class UserService {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);

    private  UserDAO userDao = new UserDAOImpl();
    private  UserProfileDAO profileDao = new UserProfileDAOImpl();

    public boolean register(String username, String fullName, String email, String password) {
        var u = new org.example.model.User(username, fullName, email, password);

        if (userDao.registerUser(u)) {
            log.info("Registered");
            return true;
        }
        log.warn("Register failed");
        return false;
    }

    public boolean login(String username, String pass) {
        boolean ok = userDao.loginUser(username, pass);
        if (ok) {
            log.info("Logged in");
            return true;
        }
        log.warn("Login failed");
        return false;
    }

    public void viewProfile(String username) {
        UserProfile p = userDao.viewProfile(username);

        if (p != null) {
            log.info("Profile:");
            System.out.println(p);
        } else {
            log.warn("No profile");
        }
    }

    public void createProfile(String username, String bio, String loc) {
        int uid = userDao.getUserIdByUsername(username);
        if (uid == -1) {
            log.warn("User not found");
            return;
        }

        UserProfile p = new UserProfile(uid, bio, loc);

        if (profileDao.addProfile(p))
            log.info("Profile added");
        else
            log.warn("Failed");
    }

    public void updateProfile(String username, String bio, String loc) {
        int uid = userDao.getUserIdByUsername(username);
        if (uid == -1) {
            log.warn("User not found");
            return;
        }

        UserProfile p = new UserProfile(uid, bio, loc);

        if (profileDao.updateProfile(p))
            log.info("Profile updated");
        else
            log.warn("Update failed");
    }

    public void forgotPassword(String username) {
        boolean sent = userDao.generateAndStoreOTP(username);
        if (!sent) {
            log.error("OTP error");
            return;
        }

        Scanner sc = new Scanner(System.in);
        log.info("Enter OTP:");
        String given = sc.nextLine();

        if (!userDao.verifyOTP(username, given)) {
            log.warn("Wrong OTP");
            return;
        }

        log.info("New password:");
        String np = sc.nextLine();

        log.info("Confirm:");
        String cp = sc.nextLine();

        if (!np.equals(cp)) {
            log.warn("Not same");
            return;
        }
        userDao.resetPassword(username, np);
    }


}
