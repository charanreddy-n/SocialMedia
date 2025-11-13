package org.example.dao;

import org.example.controller.SocialMediaAppMain;
import org.example.database.DBConnection;
import org.example.model.User;
import org.example.model.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.security.MessageDigest;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl  implements UserDAO {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);

    private Connection con;

    public UserDAOImpl() {
        con = DBConnection.getConnection();
    }

    private String hash(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(pass.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : out) sb.append(String.format("%02x", b));

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("hash error", e);
        }
    }

    @Override
    public boolean registerUser(User u) {
        String check = "SELECT * FROM users WHERE username=? OR email=?";
        String add = "INSERT INTO users(username, full_name, email, password_hash) VALUES (?,?,?,?)";

        try (PreparedStatement ps1 = con.prepareStatement(check);
             PreparedStatement ps2 = con.prepareStatement(add)) {

            ps1.setString(1, u.getUsername());
            ps1.setString(2, u.getEmail());

            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                log.warn("User/email exists");
                return false;
            }

            ps2.setString(1, u.getUsername());
            ps2.setString(2, u.getFullName());
            ps2.setString(3, u.getEmail());
            ps2.setString(4, hash(u.getPassword()));

            return ps2.executeUpdate() > 0;

        } catch (SQLException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean loginUser(String username, String password) {
        String sql = "SELECT user_id, password_hash FROM users WHERE username=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String saved = rs.getString("password_hash");

                if (saved.equals(hash(password))) {
                    recordLogin(rs.getInt("user_id"));
                    return true;
                }
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return false;
    }

    private void recordLogin(int uid) {
        String sql = "INSERT INTO login_activity(user_id) VALUES (?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public UserProfile viewProfile(String username) {
        String sql = """
            SELECT u.user_id, u.username, u.full_name, u.email,
                   p.bio, p.location
            FROM users u
            LEFT JOIN user_profile p ON u.user_id = p.user_id
            WHERE u.username=?
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new UserProfile(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("bio"),
                        rs.getString("location")
                );
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public int getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("user_id");

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return -1;
    }

    @Override
    public String getUsernameById(int id) {
        String sql = "SELECT username FROM users WHERE user_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("username");

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public boolean generateAndStoreOTP(String username) {
        String otp = String.format("%06d", new Random().nextInt(999999));

        String updateSql = "UPDATE users SET otp=? WHERE username=?";
        String emailSql = "SELECT email FROM users WHERE username=?";

        try (PreparedStatement psMail = con.prepareStatement(emailSql);
             PreparedStatement psUpdate = con.prepareStatement(updateSql)) {

            psMail.setString(1, username);
            ResultSet rs = psMail.executeQuery();

            if (!rs.next()) {
                log.warn("User not found");
                return false;
            }

            String email = rs.getString("email");

            psUpdate.setString(1, otp);
            psUpdate.setString(2, username);

            if (psUpdate.executeUpdate() > 0) {
                org.example.util.EmailUtil.sendOTP(email, otp);
                return true;
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return false;
    }

    @Override
    public boolean verifyOTP(String username, String otp) {
        String sql = "SELECT otp FROM users WHERE username=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String saved = rs.getString("otp");
                return saved != null && saved.equals(otp);
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return false;
    }

    @Override
    public boolean resetPassword(String username, String newPass) {
        String sql = "UPDATE users SET password_hash=?, otp=NULL WHERE username=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hash(newPass));
            ps.setString(2, username);

            if (ps.executeUpdate() > 0) {
                log.info("Password updated");
                return true;
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return false;
    }

    @Override
    public List<String> getFollowingUsernames(String username) {
        List<String> list = new ArrayList<>();

        String sql = """
            SELECT u.username
            FROM followers f
            JOIN users u ON f.following_id = u.user_id
            WHERE f.follower_id =
                (SELECT user_id FROM users WHERE username=?)
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("username"));
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return list;
    }

}

