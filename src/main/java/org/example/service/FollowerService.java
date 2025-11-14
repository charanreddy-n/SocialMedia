package org.example.service;

import org.example.controller.SocialMediaAppMain;
import org.example.dao.*;
import org.example.model.Follower;
import org.example.model.FriendRequest;

import java.util.List;

public class FollowerService {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);

    private FollowerDAO followDao = new FollowerDAOImpl();
    private  UserDAO userDao = new UserDAOImpl();
    private FriendRequestDAO reqDao = new FriendRequestDAOImpl();

    public void followUser(String user, String other) {
        int f1 = userDao.getUserIdByUsername(user);
        int f2 = userDao.getUserIdByUsername(other);

        if (f1 == -1 || f2 == -1) {
            log.warn("User not found");
            return;
        }

        if (f1 == f2) {
            log.warn("Can't follow self");
            return;
        }

        List<FriendRequest> list = reqDao.getPendingRequests(f2);

        boolean sent = list.stream()
                .anyMatch(r -> r.getSenderId() == f1);

        if (sent) {
            log.info("You already sent a request. Wait.");
            return;
        }
        followDao.followUser(new Follower(f1, f2));
    }

    public void unfollowUser(String user, String other) {
        int u = userDao.getUserIdByUsername(user);
        int t = userDao.getUserIdByUsername(other);

        if (u == -1 || t == -1) {
            log.warn("Wrong user");
            return;
        }

        boolean ok = followDao.unfollowUser(u, t);
        if (ok)
            log.info("Done");
        else
            log.warn("Not followed");
    }

    public void showFollowers(String username) {
        List<String> list = followDao.getFollowers(username);
        log.info("Followers:");
        if (list.isEmpty()) {
            log.info("No one");
        } else {
            list.forEach(System.out::println);
        }
    }

    public void showFollowing(String username) {
        List<String> list = followDao.getFollowing(username);
        log.info("Following:");
        if (list.isEmpty()) {
            log.info("None");
        } else {
            list.forEach(System.out::println);
        }
    }
}
