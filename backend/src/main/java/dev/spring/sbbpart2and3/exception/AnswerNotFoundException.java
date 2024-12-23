package dev.spring.sbbpart2and3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "answerEntity not found")
public class AnswerNotFoundException extends RuntimeException {
    public AnswerNotFoundException() {super("답변을 찾을 수 없습니다.");}
}
