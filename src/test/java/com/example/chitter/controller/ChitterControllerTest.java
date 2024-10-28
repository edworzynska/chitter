package com.example.chitter.controller;

import com.example.chitter.dto.Mapper;
import com.example.chitter.model.Peep;
import com.example.chitter.model.User;
import com.example.chitter.repository.PeepRepository;
import com.example.chitter.repository.UserRepository;
import com.example.chitter.service.PeepService;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChitterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PeepRepository peepRepository;

    @Autowired
    private UserRepository userRepository;

    private User user9;
    private Peep peep9;

    @BeforeEach
    @Transactional
    public void setUp() {
        peepRepository.deleteAll();
        userRepository.deleteAll();

        user9 = new User();
        user9.setUsername("user9");
        user9.setPassword("Password123!");
        user9.setEmail("email9@email.com");
        userRepository.save(user9);

        peep9 = new Peep();
        peep9.setUser(user9);
        peep9.setContents("contents");
        peep9.setDate(LocalDateTime.of(2024, 7, 14, 14, 20));
        peepRepository.save(peep9);
    }

    @Test
    void getsUserAsDto() throws Exception {

        Long userId = user9.getId();
        user9.setPeeps(List.of(peep9));
        assertNotNull(userId);

        mockMvc.perform(get("/users/{userId}/peeps", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.username").value("user9"));
    }

    @Test
    void returnsErrorIfUserDoesntExist() throws Exception{
        mockMvc.perform(get("/users/20/peeps"))
                .andExpect(status().is5xxServerError());
    }
}
