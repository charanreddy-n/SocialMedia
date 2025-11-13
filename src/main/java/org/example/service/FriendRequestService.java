package org.example.service;

import org.example.controller.SocialMediaAppMain;
import org.example.dao.FriendRequestDAO;
import org.example.dao.FriendRequestDAOImpl;
import org.example.dao.UserDAO;
import org.example.dao.UserDAOImpl;
import org.example.model.FriendRequest;

import java.util.List;

public class FriendRequestService {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);

    private  FriendRequestDAO reqDao = new FriendRequestDAOImpl();
    private  UserDAO userDao = new UserDAOImpl();

    public void sendFriendRequest(String from, String to) {
        int s = userDao.getUserIdByUsername(from);
        int r = userDao.getUserIdByUsername(to);

        if (s == -1 || r == -1) {
            log.warn("User not found");
            return;
        }

        if (s == r) {
            log.warn("Can not send to self");
            return;
        }

        if (reqDao.sendRequest(new FriendRequest(s, r, "PENDING")))
            log.info("Sent");
        else
            log.warn("Failed");
    }

    public void viewPendingRequests(String user) {
        int uid = userDao.getUserIdByUsername(user);
        List<FriendRequest> list = reqDao.getPendingRequests(uid);

        if (list.isEmpty()) {
            log.info("None");
            return;
        }

        log.info("Pending:");
        for (var r : list) {
            String sender = userDao.getUsernameById(r.getSenderId());
            log.info("ID " + r.getRequestId() + " from " + sender);
        }
    }

    public void acceptFriendRequest(String username, int id) {
        int uid = userDao.getUserIdByUsername(username);

        var req = reqDao.getRequestById(id);
        if (req == null) {
            log.warn("No request");
            return;
        }

        if (req.getReceiverId() != uid) {
            log.warn("Not yours");
            return;
        }

        if (reqDao.acceptRequest(id))
            log.info("Accepted");
        else
            log.warn("Failed");
    }
}
