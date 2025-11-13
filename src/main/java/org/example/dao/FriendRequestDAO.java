package org.example.dao;

import org.example.model.FriendRequest;
import java.util.List;

public interface FriendRequestDAO {
    boolean sendRequest(FriendRequest req);

    List<FriendRequest> getPendingRequests(int userId);

    boolean acceptRequest(int requestId);

    FriendRequest getRequestById(int requestId);
}
