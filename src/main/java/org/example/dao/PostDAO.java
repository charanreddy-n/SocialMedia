package org.example.dao;

import org.bson.Document;
import org.example.model.Post;

import java.util.List;

public interface PostDAO {
    void createPost(Post p);

    List<Document> getAllPosts();

    void likePost(String postId, String user);

    void addComment(String postId, Document c);

    List<Document> getPostsByAuthors(List<String> names);
}
