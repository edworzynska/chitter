package com.example.chitter.service;

import com.example.chitter.configuration.SecurityConfiguration;
import com.example.chitter.model.Peep;
import com.example.chitter.model.User;
import com.example.chitter.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Boolean passwordRequirements(String password){
        boolean areRequirementsMet = false;
        if (password.length() >= 8){
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            if (hasLetter.find() && hasDigit.find() && hasSpecial.find()){
                areRequirementsMet = true;
            }
        }
        return areRequirementsMet;
    }

    @Transactional
    public User createUser(String email, String username, String password){
        if (userRepository.findByEmail(email).isPresent() || userRepository.findByUsername(username).isPresent()){
            throw new EntityExistsException("User already exists!");
        }
        if (!passwordRequirements(password)){
            throw new SecurityException("Password must be at least 8 characters long, must contain at least one special character, one letter and one number!");
        }
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return user;
    }
    public User getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }
    public List<Peep> usersPeeps(User user){
        return user.getPeeps();
    }
}
