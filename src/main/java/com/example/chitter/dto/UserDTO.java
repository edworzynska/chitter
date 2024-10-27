package com.example.chitter.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private String username;

    private List<PeepDTO> peeps;
}
