package com.networkcommunity.service;

import com.networkcommunity.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import com.networkcommunity.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        User user = new User();
        user.setName("Gilvan");
        user.setEmail("gilvan@email.com");
        user.setPassword("123456");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456"))
                .thenReturn("senhaCriptografada");

        User savedUser = new User();
        savedUser.setName("Gilvan");
        savedUser.setEmail("gilvan@email.com");
        savedUser.setPassword("senhaCriptografada");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);
        User result = userService.registerUser(user);


        assertEquals("Gilvan", result.getName());
        assertEquals("gilvan@email.com", result.getEmail());
        assertEquals("senhaCriptografada", result.getPassword());

        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(any(User.class));

    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        User user = new User();
        user.setName("Gilvan");
        user.setEmail("gilvan@email.com");
        user.setPassword("123456");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(user);
        });

        assertEquals("Email já cadastrado!", exception.getMessage());

        verify(userRepository, never())
                .save(any(User.class));

    }

    @Test
    void shouldFindUserByIdSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setName("Gilvan");
        user.setEmail("gilvan@email.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertEquals("Gilvan", result.getName());
        assertEquals("gilvan@email.com", result.getEmail());

    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findUserById(99L);
        });

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(99L);

    }

}