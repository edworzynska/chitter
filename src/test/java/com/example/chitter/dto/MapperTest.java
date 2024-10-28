package com.example.chitter.dto;

import com.example.chitter.model.Peep;
import com.example.chitter.model.User;
import com.example.chitter.repository.PeepRepository;
import com.example.chitter.repository.UserRepository;
import org.h2.tools.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class MapperTest {

    private UserDTO userDTO;
    private PeepDTO peepDTO;

    @Autowired
    private Mapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PeepRepository peepRepository;

    private User user;
    private Peep peep;
    private Peep peep2;

    @BeforeAll
    public static void initTest() throws SQLException {

        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "9099")
                .start();

    }
    @AfterEach
    public void clearData(){
        peepRepository.deleteAll();
        userRepository.deleteAll();

    }

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUsername("user");
        user.setPassword("Password123!");
        user.setEmail("user@user.com");
        userRepository.save(user);

        peep = new Peep();
        peep.setUser(user);
        peep.setDate(LocalDateTime.of(2024, 5, 8, 14 ,20));
        peep.setContents("a peep");
        peepRepository.save(peep);

        peep2 = new Peep();
        peep2.setUser(user);
        peep2.setDate(LocalDateTime.of(2023, 3, 6, 11,0));
        peep2.setContents("another peep");
        peepRepository.save(peep2);

        user = userRepository.findById(user.getId()).orElseThrow();
    }

    @Test
    void userToDTO() {
        UserDTO dto = mapper.userToDTO(user, List.of(peep, peep2));
        assertEquals(2, dto.getPeeps().size());
        assertEquals(user.getUsername(), dto.getUsername());
    }

    @Test
    void peepToDTO() {
        PeepDTO dto = mapper.peepToDTO(peep);
        assertEquals("user", dto.getUsername());
        assertEquals("a peep", dto.getContents());
        assertEquals(peep.getDate(), dto.getDateTime());
    }
}