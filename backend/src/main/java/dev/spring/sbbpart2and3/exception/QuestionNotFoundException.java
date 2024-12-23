package dev.spring.sbbpart2and3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "questionEntity not found")
public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException() {
        super("질문을 찾을 수 없습니다.");
    }
}
