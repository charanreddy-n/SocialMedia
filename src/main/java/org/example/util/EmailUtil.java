package org.example.util;


import org.example.controller.SocialMediaAppMain;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtil {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);

    private static final String FROM = "charanreddyn1@gmail.com";
    private static final String PASS = "jqqj mniq izbu cgox";

    public static boolean sendOTP(String to, String otp) {

        String subject = "OTP Code";
        String body = "Your OTP is: " + otp;

        Properties p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", "587");

        Session s = Session.getInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASS);
            }
        });

        try {
            Message m = new MimeMessage(s);
            m.setFrom(new InternetAddress(FROM));
            m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            m.setSubject(subject);
            m.setText(body);

            Transport.send(m);
            log.info("OTP sent to " + to);
            return true;

        } catch (MessagingException e) {
            log.info(e.getMessage());
            return false;
        }
    }
}
