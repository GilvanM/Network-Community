package com.networkcommunity.service;

import com.networkcommunity.entity.FriendRequest;
import com.networkcommunity.entity.FriendRequestStatus;
import com.networkcommunity.entity.Post;
import com.networkcommunity.entity.User;
import com.networkcommunity.exception.PostNotFoundException;
import com.networkcommunity.exception.UserNotFoundException;
import com.networkcommunity.repository.PostRepository;
import com.networkcommunity.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void shouldCreatePostSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setName("Gilvan");
        user.setEmail("gilvan@email.com");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        postService.createPost(
                "My first post",
                user.getEmail()
        );

        ArgumentCaptor<Post> captor =
                ArgumentCaptor.forClass(Post.class);

        verify(postRepository)
                .save(captor.capture());

        Post savedPost = captor.getValue();

        assertEquals(
                "My first post",
                savedPost.getContent()
        );

        assertEquals(
                user,
                savedPost.getUser()
        );
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findByEmail("gilvan@email.com"))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> postService.createPost(
                        "Meu post",
                        "gilvan@email.com"
                )
        );

        assertEquals(
                "User not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldReturnFeedPostsForAuthenticatedUser() {

        User user = new User();
        user.setId(1L);
        user.setEmail("gilvan@email.com");

        Post ownPost = new Post();
        ownPost.setId(1L);
        ownPost.setContent("My post");
        ownPost.setUser(user);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(postRepository.findFeedPosts(user.getId()))
                .thenReturn(List.of(ownPost));

        List<Post> result =
                postService.listPosts(user.getEmail());

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                "My post",
                result.getFirst().getContent()
        );

        verify(postRepository)
                .findFeedPosts(user.getId());
    }

    @Test
    void shouldReturnEmptyFeedWhenUserHasNoPostsOrFriendsPosts() {

        User user = new User();
        user.setId(1L);
        user.setEmail("gilvan@email.com");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(postRepository.findFeedPosts(user.getId()))
                .thenReturn(List.of());

        List<Post> result =
                postService.listPosts(user.getEmail());

        assertEquals(
                0,
                result.size()
        );

        verify(postRepository)
                .findFeedPosts(user.getId());
    }

    @Test
    void shouldLikePostSuccessfully() {

        Post post = new Post();
        post.setId(1L);
        post.setLikes(0);

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        postService.likePost(1L);

        assertEquals(
                1,
                post.getLikes()
        );

        verify(postRepository)
                .save(post);
    }

    @Test
    void shouldThrowExceptionWhenPostNotFound() {

        when(postRepository.findById(99L))
                .thenReturn(Optional.empty());

        PostNotFoundException exception = assertThrows(
                PostNotFoundException.class,
                () -> postService.likePost(99L)
        );

        assertEquals(
                "Post not found",
                exception.getMessage()
        );

        verify(postRepository)
                .findById(99L);
    }
}