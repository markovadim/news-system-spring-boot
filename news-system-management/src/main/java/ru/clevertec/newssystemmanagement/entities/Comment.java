package ru.clevertec.newssystemmanagement.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "comments")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime time;
    private String text;
    private String username;

    @Column(name = "news_id")
    private long newsId;

    public Comment(LocalDateTime time, String text, String username) {
        this.time = time;
        this.text = text;
        this.username = username;
    }
}
