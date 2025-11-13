package org.example.model;

import org.bson.Document;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class Post {
    private String author;
    private String text;
    private LocalDateTime time;
    private List<String> likes;
    private List<Document> comments;

    public Post(String author, String text, String imagePath) {
        this.author = author;
        this.text = text;
        this.time = LocalDateTime.now();
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Document toDocument() {
        return new Document("author", author)
                .append("content", text)
                .append("createdAt", time.toString())
                .append("likes", likes)
                .append("comments", comments);
    }
}
