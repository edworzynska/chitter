package com.example.chitter.service;

import com.example.chitter.model.User;
import com.example.chitter.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.h2.tools.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @BeforeAll
//    public static void initTest() throws SQLException {
//
//        Server.createWebServer("-web", "", "-webPort", "8092")
//                .start();
//    }
//
//    @AfterEach
//    void deleteData(){
//        userRepository.deleteAll();
//    }

    @Test
    void createsNewUser() {
        User user = userService.createUser("ddd1@dd.com", "doo1", "pass1word!");
        assertEquals("doo1", user.getUsername());
        assertTrue(userRepository.existsById(user.getId()));
        userRepository.delete(user);
    }
    @Test
    void returnsAnErrorIfPasswordDoesntHaveCorrectLength() {
        SecurityException e = assertThrows(SecurityException.class, () -> userService.createUser("ddd@dd.com", "doo", "pass"));
        assertEquals("Password must be at least 8 characters long, must contain at least one special character, one letter and one number!", e.getMessage());

    }
    @Test
    void returnsAnErrorIfPasswordDoesntHaveNumbersOrSpecialCharacters() {
        SecurityException e2 = assertThrows(SecurityException.class, () -> userService.createUser("ddd@dd.com", "doo", "password"));
        assertEquals("Password must be at least 8 characters long, must contain at least one special character, one letter and one number!", e2.getMessage());
    }
    @Test
    void returnsAnErrorIfPasswordDoesntHaveSpecialCharacters() {
        SecurityException e3 = assertThrows(SecurityException.class, () -> userService.createUser("ddd@dd.com", "doo", "password1"));
        assertEquals("Password must be at least 8 characters long, must contain at least one special character, one letter and one number!", e3.getMessage());
    }
    @Test
    void returnsAnErrorIfPasswordDoesntHaveNumbers() {
        SecurityException e4 = assertThrows(SecurityException.class, ()-> userService.createUser("ddd@dd.com", "doo", "pass%word"));
        assertEquals("Password must be at least 8 characters long, must contain at least one special character, one letter and one number!", e4.getMessage());
    }

    @Test
    void throwsErrorIfUserAlreadyExists() {
        User user1 = userService.createUser("email@email.com", "user1", "pass%word1");
        EntityExistsException e = assertThrows(EntityExistsException.class, ()->userService.createUser("email@email.com", "user1", "pass%word1"));
        assertEquals("User already exists!", e.getMessage());
        userRepository.delete(user1);
    }
    @Test
    void throwsErrorIfUserAlreadyExists2() {
        User user1 = userService.createUser("email@email.com", "user1", "pass%word1");
        EntityExistsException e = assertThrows(EntityExistsException.class, ()->userService.createUser("email@email.com", "user2", "pass%word1"));
        assertEquals("User already exists!", e.getMessage());
        userRepository.delete(user1);
    }
    @Test
    void throwsErrorIfUserAlreadyExists3() {
        User user1 = userService.createUser("email@email.com", "user1", "pass%word1");
        EntityExistsException e = assertThrows(EntityExistsException.class, ()->userService.createUser("email2@email.com", "user1", "pass%word1"));
        assertEquals("User already exists!", e.getMessage());
        userRepository.delete(user1);
    }
}