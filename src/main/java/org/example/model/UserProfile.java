package org.example.model;

public class UserProfile extends User {
    private int profileId;
    private String bio;
    private String location;

    public UserProfile(int userId, String username, String fullName, String email,
                       String bio, String location) {
        super(username, fullName, email, null);
        setUserId(userId);
        this.bio = bio;
        this.location = location;
    }

    public UserProfile(int userId, String bio, String location) {
        setUserId(userId);
        this.bio = bio;
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return super.toString() + " | Bio: " + bio + " | Location: " + location;
    }
}
