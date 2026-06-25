package com.networkcommunity.controller;

import com.networkcommunity.entity.User;
import com.networkcommunity.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // página de cadastro
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // salvar usuário
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/";
    }

    // home listando usuários
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("users", userService.findAll());
        return "home";
    }
}