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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChitterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PeepController peepController;

    @Autowired
    private static PeepRepository peepRepository;

    @Autowired
    private static UserRepository userRepository;

    @Autowired
    private PeepService peepService;

    @Autowired
    private Mapper mapper;

    static User user9;
    static Peep peep9;

    @BeforeAll
    public static void setUp() {
        user9 = new User();
        user9.setUsername("user9");
        user9.setPassword("Password123!");
        user9.setEmail("email9@email.com");
        userRepository.save(user9);

        peep9 = new Peep();
        peep9.setUser(user9);
        peep9.setContents("contents");
        peep9.setDate(LocalDateTime.of(2024,7,14,14,20));
        peepRepository.save(peep9);

    }
}