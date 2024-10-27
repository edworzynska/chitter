package com.example.chitter.service;

import com.example.chitter.model.Peep;
import com.example.chitter.model.User;
import com.example.chitter.repository.PeepRepository;
import com.example.chitter.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class PeepServiceIntegrationTest {


    @Autowired
    UserRepository userRepository;

    @Autowired
    PeepService peepService;

    @Autowired
    PeepRepository peepRepository;

    static User user1;
    static User user2;

    @BeforeAll
    public static void initTest() throws SQLException {

        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8084")
                .start();

    }
    @BeforeEach
    @Transactional
    void setUp() {
        userRepository.deleteAll();
        peepRepository.deleteAll();

        user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@email.com");
        user1.setPassword("User1Password#");
        userRepository.save(user1);

        user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@email.com");
        user2.setPassword("User2Password#");
        userRepository.save(user2);
    }

    @Test
    void createsPeep() throws Exception{
        Peep peep = peepService.createPeep("user1", "doo bee");
        assertEquals(peepRepository.findAll().size(), 1);
        String rslt = "";
        for (Peep p : peepRepository.findByUser(user1)){
            rslt += p.getContents();
        }
        assertEquals("doo bee", rslt);
    }

    @Test
    void throwsErrorIfUserDoesntExist() throws Exception{
        EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () ->peepService.createPeep("user3", "content"));
        assertEquals("User doesn't exist!", e.getMessage());
    }

    @Test
    void throwsErrorIfContentIsEmpty() throws Exception{
        InvalidParameterException e = assertThrows(InvalidParameterException.class, ()->peepService.createPeep("user1", ""));
        assertEquals("Peep cannot be empty!", e.getMessage());
    }
}