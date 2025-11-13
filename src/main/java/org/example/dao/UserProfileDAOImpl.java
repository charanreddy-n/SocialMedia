package org.example.dao;


import org.example.controller.SocialMediaAppMain;
import org.example.database.DBConnection;
import org.example.model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileDAOImpl implements UserProfileDAO {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);


    private Connection con;

    public UserProfileDAOImpl() {
        con = DBConnection.getConnection();
    }

    @Override
    public boolean addProfile(UserProfile p) {
        String sql = "INSERT INTO user_profile(user_id, bio, location) VALUES (?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getUserId());
            ps.setString(2, p.getBio());
            ps.setString(3, p.getLocation());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            log.info(e.getMessage());
        }

        return false;
    }

    @Override
    public boolean updateProfile(UserProfile p) {
        String sql = "UPDATE user_profile SET bio=?, location=? WHERE user_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getBio());
            ps.setString(2, p.getLocation());
            ps.setInt(3, p.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            log.info(e.getMessage());
        }

        return false;
    }
}
