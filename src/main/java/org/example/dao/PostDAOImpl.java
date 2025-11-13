package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.example.database.MongoDBConnection;
import org.example.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostDAOImpl implements PostDAO {
    private final MongoCollection<Document> posts;

    public PostDAOImpl() {
        this.posts = MongoDBConnection.getDatabase().getCollection("posts");
    }

    @Override
    public void createPost(Post p) {
        posts.insertOne(p.toDocument());
    }

    @Override
    public List<Document> getAllPosts() {
        List<Document> list = new ArrayList<>();
        try (MongoCursor<Document> cur = posts.find().iterator()) {
            while (cur.hasNext()) {
                list.add(cur.next());
            }
        }
        return list;
    }

    @Override
    public void likePost(String postId, String user) {
        posts.updateOne(
                new Document("_id", new org.bson.types.ObjectId(postId)),
                new Document("$addToSet", new Document("likes", user))
        );
    }

    @Override
    public void addComment(String postId, Document c) {
        posts.updateOne(
                new Document("_id", new org.bson.types.ObjectId(postId)),
                new Document("$push", new Document("comments", c))
        );
    }

    @Override
    public List<Document> getPostsByAuthors(List<String> names) {
        List<Document> list = new ArrayList<>();

        if (names == null || names.isEmpty()) return list;

        try (MongoCursor<Document> cur =
                     posts.find(new Document("author", new Document("$in", names))).iterator()) {
            while (cur.hasNext()) {
                list.add(cur.next());
            }
        }

        return list;
    }
}
