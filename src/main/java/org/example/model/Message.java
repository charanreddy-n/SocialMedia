package org.example.model;

import java.time.LocalDateTime;

public class Message {
    private int msgId;
    private int senderId;
    private int receiverId;
    private String text;
    private LocalDateTime sentAt;

    public Message(int s, int r, String txt) {
        this.senderId = s;
        this.receiverId = r;
        this.text = txt;
        this.sentAt = LocalDateTime.now();
    }

    public int getMsgId() { return msgId; }
    public void setMsgId(int msgId) { this.msgId = msgId; }

    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getText() { return text; }
    public LocalDateTime getSentAt() { return sentAt; }
}
