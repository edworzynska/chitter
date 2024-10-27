package com.example.chitter.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Data
@EqualsAndHashCode
@Entity
@Table(name = "Peeps")
public class Peep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "contents")
    private String contents;

    public Peep() {
    }

    public Peep(User user, LocalDateTime date, String contents) {
        this.user = user;
        this.date = date;
        this.contents = contents;
    }
}
