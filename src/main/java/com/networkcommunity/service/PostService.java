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
    public void createPost(String content, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Post post = new Post();
        post.setContent(content);
        post.setUser(user);

        postRepository.save(post);
    }

    // LISTAR POSTS
    public List<Post> listPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public void likePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        post.setLikes(post.getLikes() + 1);

        postRepository.save(post);
    }
}