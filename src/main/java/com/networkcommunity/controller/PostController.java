package com.networkcommunity.controller;
import org.springframework.ui.Model;
import com.networkcommunity.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
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
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.listPosts());
        return "posts";
    }
}