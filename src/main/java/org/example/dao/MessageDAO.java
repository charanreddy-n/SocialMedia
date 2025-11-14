package org.example.dao;

import org.example.model.Message;

import java.util.List;

public interface MessageDAO {
    boolean send(Message m);
    List<Message> getConversation(int u1, int u2);

}
