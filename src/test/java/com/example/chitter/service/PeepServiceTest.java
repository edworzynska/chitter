package com.example.chitter.service;

import com.example.chitter.model.Peep;
import com.example.chitter.model.User;
import com.example.chitter.repository.PeepRepository;
import com.example.chitter.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.InvalidParameterException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PeepServiceTest {

    @Mock
    PeepRepository peepRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    PeepService peepService;

    private User mockedUser;
    private String username;
    private String peepContent;

    @BeforeEach
    void setUp() {
        username = "user";
        peepContent = "some contents";
        mockedUser = new User();
        mockedUser.setUsername(username);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createsPeep() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockedUser));

        Peep result = peepService.createPeep("user", "some contents");
        assertNotNull(result);
        assertEquals(username, result.getUser().getUsername());
        assertEquals(peepContent, result.getContents());
        assertNotNull(result.getDate());

        verify(peepRepository, times(1)).save(result);
    }
    @Test
    void throwsErrorIfUserDoesntExist(){
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        EntityNotFoundException e = assertThrows(EntityNotFoundException.class, ()->peepService.createPeep("user", "some contents"));
        assertEquals("User doesn't exist!", e.getMessage());

        verify(peepRepository, never()).save(any(Peep.class));
    }
    @Test
    void throwsErrorIfContentIsEmpty(){
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockedUser));
        InvalidParameterException e = assertThrows(InvalidParameterException.class, ()->peepService.createPeep("user", ""));
        assertEquals("Peep cannot be empty!", e.getMessage());

        verify(peepRepository, never()).save(any(Peep.class));
    }
}