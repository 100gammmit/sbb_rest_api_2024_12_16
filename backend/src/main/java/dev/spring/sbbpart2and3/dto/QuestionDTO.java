package dev.spring.sbbpart2and3.dto;

import dev.spring.sbbpart2and3.domain.Question;
import lombok.Setter;


import java.time.LocalDateTime;

public record QuestionDTO(
        Long id,
        String subject,
        String content,
        String author,
        int voterCount,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {

    public static QuestionDTO toQuestionDto(Question question) {
        return new QuestionDTO(question.getId(),
                question.getSubject(),
                question.getContent(),
                question.getAuthor() != null ? question.getAuthor().getUsername() : null,
                question.getVoter().size(),
                question.getCreatedDate(),
                question.getLastModifiedDate());
    }
}
