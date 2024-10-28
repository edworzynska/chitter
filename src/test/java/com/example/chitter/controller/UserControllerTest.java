package com.example.chitter.controller;

import com.example.chitter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearData(){
        userRepository.deleteAll();
    }

    @Test
    void returnsOkStatusIfCorrectParamsProvidedUponRegistration() throws Exception{
        mockMvc.perform(post("/users/register")
                        .param("email", "email@email.com")
                .param("username", "user")
                .param("password", "Password!3"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("User registered successfully! You can sign in."));
    }

    @Test
    void returnsErrorIfParamsAreMissing() throws Exception{
        mockMvc.perform(post("/users/register")
                        .param("email", "email@email.com")
                .param("username", "username"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Unable to process the request - missing parameter: password"));
    }
    @Test
    void returnsErrorIfPasswordDoesntMeetRequirements() throws Exception{
        mockMvc.perform(post("/users/register")
                        .param("email", "email@email.com")
                        .param("username", "username")
                        .param("password", "pass"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Password must be at least 8 characters long, must contain at least one special character, one letter and one number!"));
    }
    @Test
    void returnsErrorIfPasswordDoesntMeetRequirements2() throws Exception{
        mockMvc.perform(post("/users/register")
                        .param("email", "email@email.com")
                        .param("username", "username")
                        .param("password", "password"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Password must be at least 8 characters long, must contain at least one special character, one letter and one number!"));
    }
    @Test
    void returnsErrorIfUserWithTheSameUsernameAlreadyExists() throws Exception{
        userController.signUp("email", "username", "Password!@3");
        mockMvc.perform(post("/users/register")
                        .param("email", "email@email.com")
                        .param("username", "username")
                        .param("password", "password45&"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("User already exists!"));
    }
    @Test
    void returnsErrorIfUserWithTheSameEmailAlreadyExists() throws Exception{
        userController.signUp("email", "username", "Password!@3");
        mockMvc.perform(post("/users/register")
                        .param("email", "email")
                        .param("username", "username2")
                        .param("password", "password45&"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("User already exists!"));

    }
}