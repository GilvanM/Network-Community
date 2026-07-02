package com.networkcommunity.controller;
import org.springframework.security.core.Authentication;
import com.networkcommunity.entity.FriendRequest;
import com.networkcommunity.entity.User;
import com.networkcommunity.service.FriendRequestService;
import com.networkcommunity.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class UserProfileController {

    private final UserService userService;
    private final FriendRequestService friendRequestService;

    public UserProfileController(UserService userService,
                                 FriendRequestService friendRequestService) {
        this.userService = userService;
        this.friendRequestService = friendRequestService;
    }

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable Long id,
                             Model model,
                             Authentication auth) {

        User user = userService.findUserById(id);

        List<FriendRequest> requests =
                friendRequestService.getReceivedRequests(user.getEmail());

        model.addAttribute("user", user);
        model.addAttribute("requests", requests);

        return "profile";
    }
}