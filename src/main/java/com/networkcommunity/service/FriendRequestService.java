package com.networkcommunity.service;
import com.networkcommunity.exception.FriendRequestException;
import com.networkcommunity.exception.UserNotFoundException;
import com.networkcommunity.entity.*;
import com.networkcommunity.repository.FriendRequestRepository;
import com.networkcommunity.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository,
                                UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // SEND REQUEST
    // =========================
    public void sendRequest(String senderEmail, Long receiverId) {

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new FriendRequestException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new FriendRequestException("Receiver not found"));

        if (sender.getId().equals(receiver.getId())) {
            throw new FriendRequestException("You cannot add yourself.");
        }

        // verifica qualquer relação entre os dois usuários
        Optional<FriendRequest> existing =
                friendRequestRepository.findBetweenUsers(sender, receiver);

        if (existing.isPresent()) {

            FriendRequest fr = existing.get();

            if (fr.getStatus() == FriendRequestStatus.PENDING) {
                throw new FriendRequestException("Request already sent.");
            }

            if (fr.getStatus() == FriendRequestStatus.ACCEPTED) {
                throw new FriendRequestException("You two are already friends.");
            }

            if (fr.getStatus() == FriendRequestStatus.REJECTED) {
                fr.setStatus(FriendRequestStatus.PENDING);
                friendRequestRepository.save(fr);
                return;
            }
        }

        // cria nova solicitação
        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(FriendRequestStatus.PENDING);

        friendRequestRepository.save(request);
    }

    // =========================
    // RECEIVED REQUESTS
    // =========================
    public List<FriendRequest> getReceivedRequests(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return friendRequestRepository.findAll()
                .stream()
                .filter(fr -> fr.getReceiver().getId().equals(user.getId()))
                .filter(fr -> fr.getStatus() == FriendRequestStatus.PENDING)
                .toList();
    }

    // =========================
    // ACCEPT REQUEST
    // =========================
    public void acceptRequest(Long id) {

        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new FriendRequestException("Request not found"));

        request.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);
    }

    // =========================
    // REJECT REQUEST
    // =========================
    public void rejectRequest(Long id) {

        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new FriendRequestException("Request not found"));

        request.setStatus(FriendRequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }
}