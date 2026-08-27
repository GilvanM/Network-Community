package com.networkcommunity.controller;

import com.networkcommunity.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public String createPost(@RequestParam String content,
                             Authentication auth) {

        postService.createPost(content, auth.getName());

        return "redirect:/posts";
    }

    @GetMapping
    public String listPosts(Model model,
                            Authentication auth) {

        model.addAttribute(
                "posts",
                postService.listPosts(auth.getName())
        );

        return "posts";
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable Long id) {

        postService.likePost(id);

        return "redirect:/posts";
    }
}