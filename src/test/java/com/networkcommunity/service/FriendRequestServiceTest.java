package com.networkcommunity.service;

import com.networkcommunity.entity.FriendRequest;
import com.networkcommunity.entity.FriendRequestStatus;
import com.networkcommunity.entity.User;
import com.networkcommunity.exception.FriendRequestException;
import com.networkcommunity.exception.UserNotFoundException;
import com.networkcommunity.repository.FriendRequestRepository;
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
class FriendRequestServiceTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendRequestService friendRequestService;

    @Test
    void shouldSendFriendRequestSuccessfully() {
        User sender = new User();
        sender.setId(1L);
        sender.setEmail("gilvan@email.com");

        User receiver = new User();
        receiver.setId(2L);
        receiver.setEmail("joao@email.com");

        when(userRepository.findByEmail(sender.getEmail()))
                .thenReturn(Optional.of(sender));

        when(userRepository.findById(receiver.getId()))
                .thenReturn(Optional.of(receiver));

        when(friendRequestRepository.findBetweenUsers(sender, receiver))
                .thenReturn(Optional.empty());

        friendRequestService.sendRequest(
                sender.getEmail(),
                receiver.getId()
        );

        ArgumentCaptor<FriendRequest> captor =
                ArgumentCaptor.forClass(FriendRequest.class);

        verify(friendRequestRepository)
                .save(captor.capture());

        FriendRequest savedRequest = captor.getValue();

        assertEquals(sender, savedRequest.getSender());
        assertEquals(receiver, savedRequest.getReceiver());
        assertEquals(
                FriendRequestStatus.PENDING,
                savedRequest.getStatus()
        );
    }

    @Test
    void shouldThrowExceptionWhenSenderNotFound() {

        when(userRepository.findByEmail("gilvan@email.com"))
                .thenReturn(Optional.empty());

        FriendRequestException exception = assertThrows(
                FriendRequestException.class,
                () -> friendRequestService.sendRequest(
                        "gilvan@email.com",
                        2L
                )
        );

        assertEquals(
                "Sender not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenReceiverNotFound() {

        User sender = new User();
        sender.setId(1L);
        sender.setEmail("gilvan@email.com");

        when(userRepository.findByEmail(sender.getEmail()))
                .thenReturn(Optional.of(sender));

        when(userRepository.findById(2L))
                .thenReturn(Optional.empty());

        FriendRequestException exception = assertThrows(
                FriendRequestException.class,
                () -> friendRequestService.sendRequest(
                        sender.getEmail(),
                        2L
                )
        );

        assertEquals(
                "Receiver not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldAcceptFriendRequestSuccessfully() {

        FriendRequest request = new FriendRequest();
        request.setStatus(FriendRequestStatus.PENDING);

        when(friendRequestRepository.findById(1L))
                .thenReturn(Optional.of(request));

        friendRequestService.acceptRequest(1L);

        assertEquals(
                FriendRequestStatus.ACCEPTED,
                request.getStatus()
        );

        verify(friendRequestRepository)
                .save(request);
    }

    @Test
    void shouldRejectFriendRequestSuccessfully() {

        FriendRequest request = new FriendRequest();
        request.setStatus(FriendRequestStatus.PENDING);

        when(friendRequestRepository.findById(1L))
                .thenReturn(Optional.of(request));

        friendRequestService.rejectRequest(1L);

        assertEquals(
                FriendRequestStatus.REJECTED,
                request.getStatus()
        );

        verify(friendRequestRepository)
                .save(request);
    }

    @Test
    void shouldReturnReceivedRequestsSuccessfully() {

        User user = new User();
        user.setId(2L);
        user.setEmail("joao@email.com");

        FriendRequest request = new FriendRequest();
        request.setReceiver(user);
        request.setStatus(FriendRequestStatus.PENDING);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(friendRequestRepository.findAll())
                .thenReturn(List.of(request));

        List<FriendRequest> result =
                friendRequestService.getReceivedRequests(
                        user.getEmail()
                );

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                FriendRequestStatus.PENDING,
                result.getFirst().getStatus()
        );

        verify(friendRequestRepository)
                .findAll();
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnGetReceivedRequests() {

        when(userRepository.findByEmail("joao@email.com"))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> friendRequestService.getReceivedRequests(
                        "joao@email.com"
                )
        );

        assertEquals(
                "User not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenRequestNotFoundOnAccept() {

        when(friendRequestRepository.findById(99L))
                .thenReturn(Optional.empty());

        FriendRequestException exception = assertThrows(
                FriendRequestException.class,
                () -> friendRequestService.acceptRequest(99L)
        );

        assertEquals(
                "Request not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenRequestNotFoundOnReject() {

        when(friendRequestRepository.findById(99L))
                .thenReturn(Optional.empty());

        FriendRequestException exception = assertThrows(
                FriendRequestException.class,
                () -> friendRequestService.rejectRequest(99L)
        );

        assertEquals(
                "Request not found",
                exception.getMessage()
        );
    }
}