package com.example.chitter.dto;

import com.example.chitter.model.Peep;
import com.example.chitter.model.User;
import com.example.chitter.repository.PeepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {


    public UserDTO userToDTO(User user, List<Peep> usersPeeps) {
        List<PeepDTO> usersPeepsDTO = new ArrayList<>();
        for (Peep peep : usersPeeps){
            usersPeepsDTO.add(peepToDTO(peep));
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPeeps(usersPeepsDTO);

        return userDTO;
    }

    public PeepDTO peepToDTO(Peep peep){

        PeepDTO peepDTO = new PeepDTO();
        peepDTO.setDateTime(peep.getDate());
        peepDTO.setContents(peep.getContents());
        peepDTO.setUsername(peep.getUser().getUsername());

        return peepDTO;
    }
}
