package com.example.chitter.controller;

import com.example.chitter.service.UserService;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> signUp(@RequestParam String email, @RequestParam String username, @RequestParam String password){
        try {
            userService.createUser(email, username, password);
            return new ResponseEntity<>("User registered successfully! You can sign in.", HttpStatus.OK);
        } catch (EntityExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (SecurityException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred. Please try again.", HttpStatus.BAD_REQUEST);
        }
    }
}
