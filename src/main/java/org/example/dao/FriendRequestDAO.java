package org.example.dao;

import org.example.model.FriendRequest;
import java.util.List;

public interface FriendRequestDAO {
    boolean sendRequest(FriendRequest req);

    List<FriendRequest> getPendingRequests(int userId);

    boolean acceptRequest(int requestId);
    boolean isFriends(int u1, int u2);

    FriendRequest getRequestById(int requestId);
}
