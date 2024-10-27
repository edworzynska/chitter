package com.example.chitter.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PeepDTO {

    private LocalDateTime dateTime;

    private String contents;

    private String username;
}
