package org.example.model;

public class FriendRequest {
    private int requestId;
    private int senderId;
    private int receiverId;
    private String status;

    public FriendRequest(int senderId, int receiverId, String status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int id) {
        this.requestId = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Request from " + senderId + " to " + receiverId + " (" + status + ")";
    }
}
