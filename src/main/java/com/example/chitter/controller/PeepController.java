package com.example.chitter.controller;

import com.example.chitter.dto.Mapper;
import com.example.chitter.dto.PeepDTO;
import com.example.chitter.model.Peep;
import com.example.chitter.service.PeepService;
import com.example.chitter.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;

@RestController
@RequestMapping
public class PeepController {

    @Autowired
    PeepService peepService;

    @Autowired
    Mapper mapper;

    @PostMapping("/create-peep")
    public ResponseEntity<String> addPeep(@RequestParam String contents){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            var loggedUser = authentication.getName();
            Peep peep = peepService.createPeep(loggedUser, contents);
            return new ResponseEntity<>("Peep posted successfully!", HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidParameterException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @GetMapping("peeps/{id}")
    public ResponseEntity<PeepDTO> getPeep(@PathVariable Long id){
        Peep peep = peepService.getPeep(id);
        PeepDTO peepDTO = mapper.peepToDTO(peep);
        return new ResponseEntity<>(peepDTO, HttpStatus.OK);

    }
}
