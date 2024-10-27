package com.example.chitter.repository;

import com.example.chitter.model.Peep;
import com.example.chitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeepRepository extends JpaRepository<Peep, Long> {
    List<Peep> findByUser(User user);
    List<Peep> findByUserId(Long id);

}
