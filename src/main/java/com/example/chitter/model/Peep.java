package com.example.chitter.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Entity
@Table(name = "Peeps")
public class Peep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    @Column(name = "date")
    private LocalDateTime date;

    @Getter
    @Setter
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
