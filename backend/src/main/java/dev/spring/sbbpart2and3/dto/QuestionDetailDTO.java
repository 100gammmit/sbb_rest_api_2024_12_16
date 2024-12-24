package dev.spring.sbbpart2and3.dto;

import org.springframework.data.domain.Page;

public record QuestionDetailDTO(QuestionDTO questionDTO, Page<AnswerDTO> answerDTOPage) {

}
