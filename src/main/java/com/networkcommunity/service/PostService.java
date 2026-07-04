package com.networkcommunity.service;

import com.networkcommunity.entity.Post;
import com.networkcommunity.entity.User;
import com.networkcommunity.repository.PostRepository;
import com.networkcommunity.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // CRIAR POST
    public Post createPost(String content, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setContent(content);
        post.setUser(user);

        return postRepository.save(post);
    }

    // LISTAR POSTS
    public List<Post> listPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
}