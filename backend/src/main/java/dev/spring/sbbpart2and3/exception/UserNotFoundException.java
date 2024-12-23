package dev.spring.sbbpart2and3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "userEntity not found")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }
}
