import org.example.dao.PostDAO;
import org.example.dao.UserDAO;
import org.example.model.Post;
import org.example.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostDAO postDao;

    @Mock
    private UserDAO userDao;

    @InjectMocks
    private PostService service;

    @Test
    void testCreatePost() {
        service.createPost("charan", "Where ever you... Network follow you..");
        Mockito.verify(postDao).createPost(Mockito.any());
    }

    @Test
    void testViewFeed() {
        Mockito.when(postDao.getAllPosts()).thenReturn(Collections.emptyList());
        service.viewFeed();
        Mockito.verify(postDao).getAllPosts();
    }

    @Test
    void testLikePost() {
        service.likePost("123", "charan");
        Mockito.verify(postDao).likePost("123", "charan");
    }

    @Test
    void testCommentPost() {
        service.commentOnPost("123", "varun", "Abbahhhhh....Superrr....");
        Mockito.verify(postDao).addComment(Mockito.eq("123"), Mockito.any());
    }

    @Test
    void testPersonalizedFeed() {
        Mockito.when(userDao.getFollowingUsernames("Chethan")).thenReturn(Collections.emptyList());
        Mockito.when(postDao.getPostsByAuthors(Collections.emptyList())).thenReturn(Collections.emptyList());

        service.viewPersonalizedFeed("Chethan");

        Mockito.verify(userDao).getFollowingUsernames("Chethan");
        Mockito.verify(postDao).getPostsByAuthors(Collections.emptyList());
    }
}
