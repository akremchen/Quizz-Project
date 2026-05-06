package com.quizz.quizservice.entity;


import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;
    private boolean correct;

    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;
}
