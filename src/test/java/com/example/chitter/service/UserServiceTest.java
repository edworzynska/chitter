package com.example.chitter.service;

import com.example.chitter.model.User;
import com.example.chitter.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addsUserIfNotPresent() {
        String email = "test@example.com";
        String username = "test_username";
        String password = "!password7";
        String encodedPassword = "Encoded!password7";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        User result = userService.createUser(email, username, password);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(username, result.getUsername());
        assertEquals(encodedPassword, result.getPassword());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void throwsExceptionIfUsernameExists() {
        String email = "test@example.com";
        String username = "test_username";
        String password = "!password7";
        String encodedPassword = "Encoded!password7";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        EntityExistsException e = assertThrows(EntityExistsException.class, ()->userService.createUser(email, username, password));

        assertEquals("User already exists!", e.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void throwsExceptionIfEmailExists() {
        String email = "test@example.com";
        String username = "test_username";
        String password = "!password7";
        String encodedPassword = "Encoded!password7";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        EntityExistsException e = assertThrows(EntityExistsException.class, ()->userService.createUser(email, username, password));

        assertEquals("User already exists!", e.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}