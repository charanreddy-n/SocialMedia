package org.example.service;

import org.bson.Document;
import org.example.controller.SocialMediaAppMain;
import org.example.dao.PostDAO;
import org.example.dao.UserDAO;

import org.example.dao.PostDAOImpl;
import org.example.dao.UserDAOImpl;
import org.example.model.Comment;
import org.example.model.Post;

import java.util.List;

public class PostService {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(SocialMediaAppMain.class);

    private PostDAO postDao = new PostDAOImpl();
    private UserDAO userDao = new UserDAOImpl();

    public void createPost(String author, String text) {
        postDao.createPost(new Post(author, text, null));
        log.info("Posted");
    }

    public void viewFeed() {
        List<Document> list = postDao.getAllPosts();

        if (list.isEmpty()) {
            log.info("No posts");
            return;
        }

        for (var d : list) {
            log.info("Author: " + d.getString("author"));
            log.info("Content: " + d.getString("content"));
            log.info("Likes: " + ((List<?>) d.get("likes")).size());
            log.info("Comments: " + ((List<?>) d.get("comments")).size());
            log.info("ID: " + d.getObjectId("_id").toHexString());
        }
    }

    public void likePost(String postId, String user) {
        postDao.likePost(postId, user);
        log.info("Liked");
    }

    public void commentOnPost(String postId, String user, String text) {
        Comment c = new Comment(user, text);
        postDao.addComment(postId, c.toDocument());
        log.info("Comment added");
    }

    public void viewPersonalizedFeed(String user) {
        List<String> followList = userDao.getFollowingUsernames(user);
        List<Document> posts = postDao.getPostsByAuthors(followList);

        if (posts.isEmpty()) {
            log.info("Nothing here");
            return;
        }

        for (var d : posts) {
            log.info("Author: " + d.getString("author"));
            log.info("Content: " + d.getString("content"));
            log.info("Likes: " + ((List<?>) d.get("likes")).size());
            log.info("Comments: " + ((List<?>) d.get("comments")).size());
            log.info("ID: " + d.getObjectId("_id").toHexString());
            log.info("---------------------------");
        }
    }
}
