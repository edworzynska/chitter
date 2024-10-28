package com.example.chitter.service;

import com.example.chitter.model.Peep;
import com.example.chitter.model.User;
import com.example.chitter.repository.PeepRepository;
import com.example.chitter.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.Authenticator;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PeepService {

    @Autowired
    private PeepRepository peepRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Peep createPeep(String username, String contents){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist!"));
        if (contents.isEmpty()){
            throw new InvalidParameterException("Peep cannot be empty!");
        }
        Peep peep = new Peep();
        peep.setUser(user);
        peep.setDate(LocalDateTime.now());
        peep.setContents(contents);
        peepRepository.save(peep);

        return peep;
    }
    public Peep getPeep(Long id){
        if (peepRepository.findById(id).isPresent()){
            return peepRepository.findById(id).get();
        }
        else throw new EntityNotFoundException("Peep not found!");
    }
}
