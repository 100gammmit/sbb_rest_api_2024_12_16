package dev.spring.sbbpart2and3.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserForm {
    @NotEmpty(message = "id는 필수 항목입니다.")
    private String username;

    @NotEmpty(message = "비밀먼호는 필수 항목입니다.")
    private String password1;

    private String password2;

    @NotEmpty(message = "e-mail은 필수 항목입니다.")
    @Email
    private String email;
}
