package com.networkcommunity.service;

import com.networkcommunity.entity.User;
import com.networkcommunity.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
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

        when(userRepository.save(user))
                .thenReturn(savedUser);

        User result = userService.registerUser(user);

        assertEquals("Gilvan", result.getName());
        assertEquals("gilvan@email.com", result.getEmail());
        assertEquals("senhaCriptografada", result.getPassword());

        verify(passwordEncoder)
                .encode("123456");

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        User user = new User();
        user.setName("Gilvan");
        user.setEmail("gilvan@email.com");
        user.setPassword("123456");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.registerUser(user)
        );

        assertEquals(
                "Email já cadastrado!",
                exception.getMessage()
        );

        verify(userRepository, never())
                .save(user);
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

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.findUserById(99L)
        );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(userRepository)
                .findById(99L);
    }

    @Test
    void shouldReturnAllUsersSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setName("Gilvan");
        user.setEmail("gilvan@email.com");

        Pageable pageable = PageRequest.of(0, 10);

        Page<User> page = new PageImpl<>(
                List.of(user),
                pageable,
                1
        );

        when(userRepository.findAll(pageable))
                .thenReturn(page);

        Page<User> result = userService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(
                "Gilvan",
                result.getContent().getFirst().getName()
        );

        verify(userRepository)
                .findAll(pageable);
    }

    @Test
    void shouldFindUserByEmailSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setName("Gilvan");
        user.setEmail("gilvan@email.com");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Optional<User> result =
                userService.findByEmail(user.getEmail());

        assertTrue(result.isPresent());
        assertEquals(
                "Gilvan",
                result.get().getName()
        );

        assertEquals(
                "gilvan@email.com",
                result.get().getEmail()
        );

        verify(userRepository)
                .findByEmail(user.getEmail());
    }
}