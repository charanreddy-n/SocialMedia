package org.example.service;

import org.example.controller.SocialMediaAppMain;
import org.example.dao.*;
import org.example.model.FriendRequest;
import org.example.model.Message;

import java.util.List;

public class MessageService {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);
    private FriendRequestDAO reqDao = new FriendRequestDAOImpl();



    private MessageDAO msgDao = new MessageDAOImpl();
    private UserDAO userDao = new UserDAOImpl();

    public void sendMsg(String from, String to, String txt) {
        int s = userDao.getUserIdByUsername(from);
        int r = userDao.getUserIdByUsername(to);

        if (s == -1 || r == -1) {
            log.warn("User not found");
            return;
        }
//        List<FriendRequest> list = reqDao.getPendingRequests(r);
//        boolean ok = reqDao.isFriends(s, r);
//
//        if (!ok) {
//            log.warn("You can message only friends");
//            return;
//        }
        boolean k = msgDao.send(new Message(s, r, txt));
        if (k)
            log.info("Sent");
        else
            log.warn("Failed");
    }


    public void showChat(String u1, String u2) {
        int x = userDao.getUserIdByUsername(u1);
        int y = userDao.getUserIdByUsername(u2);

        if (x == -1 || y == -1) {
            log.warn("User not found");
            return;
        }

        List<Message> list = msgDao.getConversation(x, y);
        log.info("Chat:");
        if (list.isEmpty()) {
            log.info("No messages");
            return;
        }

        for (Message m : list) {
            String name = userDao.getUsernameById(m.getSenderId());
            log.info(name + ": " + m.getText());
        }
    }
}
