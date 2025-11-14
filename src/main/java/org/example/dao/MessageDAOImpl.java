package org.example.dao;

import org.example.controller.SocialMediaAppMain;
import org.example.database.DBConnection;
import org.example.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl implements MessageDAO {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);

    private Connection conn;

    public MessageDAOImpl() {
        conn = DBConnection.getConnection();
    }

    @Override
    public boolean send(Message m) {

        String q = "INSERT INTO messages(sender_id, receiver_id, msg_text) VALUES(?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(q)) {

            ps.setInt(1, m.getSenderId());
            ps.setInt(2, m.getReceiverId());
            ps.setString(3, m.getText());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

    @Override
    public List<Message> getConversation(int u1, int u2) {

        List<Message> list = new ArrayList<>();

        String q = "SELECT * FROM messages " +
                "WHERE (sender_id=? AND receiver_id=?) " +
                "OR (sender_id=? AND receiver_id=?) " +
                "ORDER BY sent_at";

        try (PreparedStatement ps = conn.prepareStatement(q)) {

            ps.setInt(1, u1);
            ps.setInt(2, u2);
            ps.setInt(3, u2);
            ps.setInt(4, u1);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message m = new Message(
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getString("msg_text")
                );
                m.setMsgId(rs.getInt("msg_id"));
                list.add(m);
            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return list;
    }
}
