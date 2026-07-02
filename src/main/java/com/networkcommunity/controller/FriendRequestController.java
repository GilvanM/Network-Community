package com.networkcommunity.controller;

import com.networkcommunity.service.FriendRequestService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/friends")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping("/request/{receiverId}")
    public String sendRequest(@PathVariable Long receiverId,
                              Authentication auth) {

        friendRequestService.sendRequest(auth.getName(), receiverId);
        return "redirect:/users";
    }

    @GetMapping("/requests")
    public String requests(Authentication auth, Model model) {

        model.addAttribute("requests",
                friendRequestService.getReceivedRequests(auth.getName()));

        return "friend-requests";
    }

    @PostMapping("/accept/{id}")
    public String accept(@PathVariable Long id) {
        friendRequestService.acceptRequest(id);
        return "redirect:/friends/requests";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id) {
        friendRequestService.rejectRequest(id);
        return "redirect:/friends/requests";
    }
}