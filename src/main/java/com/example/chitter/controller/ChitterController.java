package com.example.chitter.controller;

import com.example.chitter.dto.Mapper;
import com.example.chitter.dto.UserDTO;
import com.example.chitter.model.Peep;
import com.example.chitter.model.User;
import com.example.chitter.service.PeepService;
import com.example.chitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/home")
@RestController
public class ChitterController {

    @Autowired
    private UserService userService;

    @Autowired
    private PeepService peepService;

    @Autowired
    private Mapper mapper;

    @GetMapping("/{userId}/peeps")
    public ResponseEntity<UserDTO> getUserWithPeeps(@PathVariable Long userId){
        User user = userService.getUser(userId);
        List<Peep> usersPeeps = userService.usersPeeps(user);
        UserDTO userDto = mapper.userToDTO(user, usersPeeps);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


}
