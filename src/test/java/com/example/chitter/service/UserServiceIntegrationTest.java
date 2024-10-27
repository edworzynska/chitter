package com.example.chitter.service;

import com.example.chitter.model.User;
import com.example.chitter.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.h2.tools.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeAll
    public static void initTest() throws SQLException {

        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082")
                .start();
    }

    @AfterEach
    void deleteData(){
        userRepository.deleteAll();
    }

    @Test
    void createsNewUser() {
        User user = userService.createUser("ddd@dd.com", "doo", "pass1word!");
        assertEquals(userRepository.count(), 1);
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
    }
    @Test
    void throwsErrorIfUserAlreadyExists2() {
        User user1 = userService.createUser("email@email.com", "user1", "pass%word1");
        EntityExistsException e = assertThrows(EntityExistsException.class, ()->userService.createUser("email@email.com", "user2", "pass%word1"));
        assertEquals("User already exists!", e.getMessage());
    }
    @Test
    void throwsErrorIfUserAlreadyExists3() {
        User user1 = userService.createUser("email@email.com", "user1", "pass%word1");
        EntityExistsException e = assertThrows(EntityExistsException.class, ()->userService.createUser("email2@email.com", "user1", "pass%word1"));
        assertEquals("User already exists!", e.getMessage());
    }
}