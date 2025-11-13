package org.example.dao;

import org.example.controller.SocialMediaAppMain;
import org.example.database.DBConnection;
import org.example.model.Follower;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FollowerDAOImpl implements FollowerDAO {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);

    private Connection con;

    public FollowerDAOImpl() {
        con = DBConnection.getConnection();
    }

    @Override
    public boolean followUser(Follower f) {
        String sql = "INSERT INTO followers(follower_id, following_id) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, f.getFollowerId());
            ps.setInt(2, f.getFollowingId());
            ps.executeUpdate();
            log.info("Followed");
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            log.info("Already following or invalid");
            return false;

        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean unfollowUser(int followerId, int followingId) {
        String sql = "DELETE FROM followers WHERE follower_id=? AND following_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, followerId);
            ps.setInt(2, followingId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                log.info("Unfollowed");
                return true;
            }
            log.info("Not following");
            return false;

        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> getFollowers(String username) {
        List<String> list = new ArrayList<>();

        String sql = """
            SELECT u2.username AS follower_name
            FROM followers f
            JOIN users u1 ON f.following_id = u1.user_id
            JOIN users u2 ON f.follower_id = u2.user_id
            WHERE u1.username = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("follower_name"));
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return list;
    }

    @Override
    public List<String> getFollowing(String username) {
        List<String> list = new ArrayList<>();

        String sql = """
            SELECT u2.username AS following_name
            FROM followers f
            JOIN users u1 ON f.follower_id = u1.user_id
            JOIN users u2 ON f.following_id = u2.user_id
            WHERE u1.username = ?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("following_name"));
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return list;
    }
}
