package org.example.dao;

import org.example.model.Follower;
import java.util.List;

public interface FollowerDAO {
    boolean followUser(Follower f);

    boolean unfollowUser(int followerId, int followingId);

    List<String> getFollowers(String username);
    List<String> getFollowing(String username);

}
