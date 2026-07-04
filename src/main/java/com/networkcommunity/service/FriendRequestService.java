package com.networkcommunity.service;

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
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("Você não pode adicionar você mesmo");
        }

        // verifica qualquer relação entre os dois usuários
        Optional<FriendRequest> existing =
                friendRequestRepository.findBetweenUsers(sender, receiver);

        if (existing.isPresent()) {

            FriendRequest fr = existing.get();

            if (fr.getStatus() == FriendRequestStatus.PENDING) {
                throw new RuntimeException("Solicitação já enviada");
            }

            if (fr.getStatus() == FriendRequestStatus.ACCEPTED) {
                throw new RuntimeException("Vocês já são amigos");
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
                .orElseThrow(() -> new RuntimeException("User not found"));

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
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);
    }

    // =========================
    // REJECT REQUEST
    // =========================
    public void rejectRequest(Long id) {

        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(FriendRequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }
}