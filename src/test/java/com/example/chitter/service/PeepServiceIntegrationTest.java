package com.example.chitter.service;

import com.example.chitter.model.Peep;
import com.example.chitter.model.User;
import com.example.chitter.repository.PeepRepository;
import com.example.chitter.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.h2.tools.Server;
import org.junit.jupiter.api.AfterEach;
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
    private UserRepository userRepository;

    @Autowired
    private PeepService peepService;

    @Autowired
    private PeepRepository peepRepository;

    private static User user1;
    private static User user2;

    @BeforeAll
    public static void initTest() throws SQLException {

        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "9094")
                .start();

    }
    @BeforeEach
    @Transactional
    void setUp() {
        user1 = new User();
        user1.setUsername("user111");
        user1.setEmail("user11@email.com");
        user1.setPassword("User1Password#");
        userRepository.save(user1);

        user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@email.com");
        user2.setPassword("User2Password#");
        userRepository.save(user2);
    }
    @AfterEach
    void clearUp(){
        userRepository.delete(user1);
        userRepository.delete(user2);
    }

    @Test
    void createsPeep() throws Exception{
        Peep peep = peepService.createPeep("user111", "doo bee");
        assertEquals(peepRepository.findAll().size(), 1);
        String rslt = "";
        for (Peep p : peepRepository.findByUser(user1)){
            rslt += p.getContents();
        }
        assertEquals("doo bee", rslt);
        peepRepository.delete(peep);
    }

    @Test
    void throwsErrorIfUserDoesntExist() throws Exception{
        EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () ->peepService.createPeep("user3", "content"));
        assertEquals("User doesn't exist!", e.getMessage());
    }

    @Test
    void throwsErrorIfContentIsEmpty() throws Exception{
        InvalidParameterException e = assertThrows(InvalidParameterException.class, ()->peepService.createPeep("user111", ""));
        assertEquals("Peep cannot be empty!", e.getMessage());
    }
    @Test
    void returnsPeepByItsId() throws Exception{
        Peep peep = peepService.createPeep("user111", "some contents");
        Peep result = peepService.getPeep(peep.getId());
        assertEquals(peep.getId(), result.getId());
        assertEquals("user111", result.getUser().getUsername());
        peepRepository.delete(peep);
    }
    @Test
    void returnsErrorIfPeepDoesntExist() throws Exception{
        EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () -> peepService.getPeep(10L));
        assertEquals("Peep not found!", e.getMessage());

    }
}