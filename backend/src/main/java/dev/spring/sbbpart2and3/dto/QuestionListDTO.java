package dev.spring.sbbpart2and3.dto;

import dev.spring.sbbpart2and3.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public record QuestionListDTO(
        Long id,
        String subject,
        LocalDateTime createdDate,
        int answerCount,
        String author
) {

    public static QuestionListDTO toQuestionListDTO(Question question) {
        return new QuestionListDTO(question.getId(),
                question.getSubject(),
                question.getCreatedDate(),
                question.getAnswerList().size(),
                question.getAuthor() != null ? question.getAuthor().getUsername() : null);
    }
}
