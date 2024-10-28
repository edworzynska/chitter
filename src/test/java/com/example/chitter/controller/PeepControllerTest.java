package com.example.chitter.controller;

import com.example.chitter.model.Peep;
import com.example.chitter.model.User;
import com.example.chitter.repository.PeepRepository;
import com.example.chitter.repository.UserRepository;
import com.example.chitter.service.PeepService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PeepControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PeepController peepController;

    @Autowired
    private PeepRepository peepRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PeepService peepService;

    @BeforeEach
    public void setUp(){
        peepRepository.deleteAll();
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("user");
        user.setPassword("Password123!");
        user.setEmail("email@email.com");
        userRepository.save(user);

    }
    @Test
    @WithAnonymousUser
    void getsPeep() throws Exception {
        Peep peep8 = new Peep();
        peep8 = peepService.createPeep("user", "contents23");
        var id = peep8.getId();
        mockMvc.perform(get("/peeps/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser(username = "user", password = "Password123!")
    void returnsOkStatusIfPeepIsPosted() throws Exception {
        mockMvc.perform(post("/create-peep").param("contents","test peep"))
                .andExpect(status().isOk())
                .andExpect(content().string("Peep posted successfully!"));
    }
    @Test
    @WithAnonymousUser
    void redirectsAnonymousUser() throws Exception {
        mockMvc.perform(post("/create-peep").param("contents","test peep"))
                .andExpect(status().is3xxRedirection());
    }
    @Test
    @WithMockUser(username = "user", password = "Password123!")
    void postsPeepAsLoggedInUser() throws Exception {
        mockMvc.perform(post("/create-peep").param("contents","test peep"))
                .andExpect(status().isOk())
                .andExpect(content().string("Peep posted successfully!"));
        Optional <User> userOptional = userRepository.findByUsername("user");
        User user = userOptional.get();
        List<Peep> peepsPostedByUser = peepRepository.findByUser(user);
        String allContents = "";
        for (Peep peep : peepsPostedByUser){
            allContents += peep.getContents();
        }
        assertEquals(1, peepsPostedByUser.size());
        assertEquals("test peep", allContents);
    }
    @Test
    @WithMockUser(username = "user", password = "Password123!")
    void returnsErrorIfContentsAreEmpty() throws Exception {
        mockMvc.perform(post("/create-peep").param("contents",""))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Peep cannot be empty!"));
    }
    @Test
    @WithMockUser(username = "userrr", password = "Password123!")
    void returnsErrorIfUserDoesntExist() throws Exception {
        mockMvc.perform(post("/create-peep").param("contents","doo"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("User doesn't exist!"));
    }

    @Test
    @WithAnonymousUser
    void returnsErrorIfNoPeepUnderEndpoint() throws Exception {
        mockMvc.perform(get("/peeps/10"))
                .andExpect(status().is5xxServerError());
    }
}