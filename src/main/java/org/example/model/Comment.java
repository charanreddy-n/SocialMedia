package org.example.model;


import org.bson.Document;

import java.time.LocalDateTime;

public class Comment {
    private String user;
    private String text;
    private LocalDateTime time;

    public Comment(String user, String text) {
        this.user = user;
        this.text = text;
        this.time = LocalDateTime.now();
    }

    public Document toDocument() {
        return new Document("user", user)
                .append("text", text)
                .append("time", time.toString());
    }
}
