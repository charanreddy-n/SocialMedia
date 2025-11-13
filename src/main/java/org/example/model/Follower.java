package org.example.model;

public class Follower {
    private int followerId;
    private int followingId;

    public Follower(int followerId, int followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public int getFollowerId() {
        return followerId;
    }

    public int getFollowingId() {
        return followingId;
    }

    @Override
    public String toString() {
        return "Follower: " + followerId + " -> " + followingId;
    }
}
