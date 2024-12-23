package dev.spring.sbbpart2and3.dto;

import dev.spring.sbbpart2and3.domain.Answer;

import java.time.LocalDateTime;


public record AnswerDTO(
        Long id,
        String content,
        String author,
        Long questionId,
        int voterCount,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {

    public static AnswerDTO toAnswerDto(Answer answer) {
        return new AnswerDTO(answer.getId(),
                answer.getContent(),
                answer.getAuthor() != null ? answer.getAuthor().getUsername() : null,
                answer.getQuestion().getId(),
                answer.getVoter().size(),
                answer.getCreatedDate(),
                answer.getLastModifiedDate());
    }
}
