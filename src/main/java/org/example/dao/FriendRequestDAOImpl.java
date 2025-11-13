package org.example.dao;

import org.example.controller.SocialMediaAppMain;
import org.example.database.DBConnection;
import org.example.model.FriendRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestDAOImpl implements FriendRequestDAO {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);

    public FriendRequestDAOImpl() {}

    @Override
    public boolean sendRequest(FriendRequest req) {
        String sql = "INSERT INTO friend_requests (sender_id, receiver_id, status) VALUES (?, ?, 'PENDING')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, req.getSenderId());
            ps.setInt(2, req.getReceiverId());
            ps.executeUpdate();
            log.info("Request sent");
            return true;

        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public List<FriendRequest> getPendingRequests(int userId) {
        List<FriendRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM friend_requests WHERE receiver_id = ? AND status = 'PENDING'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FriendRequest r = new FriendRequest(
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("status")
                );
                r.setRequestId(rs.getInt("request_id"));
                list.add(r);
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return list;
    }

    @Override
    public FriendRequest getRequestById(int requestId) {
        String sql = "SELECT * FROM friend_requests WHERE request_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                FriendRequest r = new FriendRequest(
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("status")
                );
                r.setRequestId(rs.getInt("request_id"));
                return r;
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public boolean acceptRequest(int requestId) {
        String sql = "UPDATE friend_requests SET status = 'ACCEPTED' WHERE request_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
