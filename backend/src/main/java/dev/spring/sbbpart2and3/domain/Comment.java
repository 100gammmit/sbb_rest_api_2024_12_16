package dev.spring.sbbpart2and3.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String content;

    @ManyToOne @Setter
    private Question question;

    @ManyToOne @Setter
    private Answer answer;

    @ManyToOne
    private SiteUser author;

    public Comment(String content) {
        this.content = content;
    }

}
