package dev.spring.sbbpart2and3.controller;

import dev.spring.sbbpart2and3.form.CreateUserForm;
import dev.spring.sbbpart2and3.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @Test
    void createUser_ValidInput_Success() {
        // Given
        CreateUserForm form = new CreateUserForm();
        form.setUsername("testUser");
        form.setPassword1("password123");
        form.setPassword2("password123");
        form.setEmail("test@example.com");

        when(bindingResult.hasErrors()).thenReturn(false);

        // When
        String result = userController.createUser(form, bindingResult);

        // Then
        assertEquals("redirect:/", result);

        verify(userService).createUser("testUser", "password123", "test@example.com");
    }

    @Test
    void createUser_BindingResultHasErrors() {
        // Given
        CreateUserForm form = new CreateUserForm();
        when(bindingResult.hasErrors()).thenReturn(true);

        // When
        String result = userController.createUser(form, bindingResult);

        // Then
        assertEquals("signup_form", result);
        verify(userService, never()).createUser(anyString(), anyString(), anyString());
    }

    @Test
    void createUser_PasswordsDoNotMatch() {
        // Given
        CreateUserForm form = new CreateUserForm();
        form.setPassword1("password123");
        form.setPassword2("differentPassword");

        when(bindingResult.hasErrors()).thenReturn(false);

        // When
        String result = userController.createUser(form, bindingResult);

        // Then
        assertEquals("signup_form", result);
        verify(bindingResult).rejectValue("password2", "Passwords don't match",
                "2개의 비밀번호가 일치하지 않습니다.");
        verify(userService, never()).createUser(anyString(), anyString(), anyString());
    }

    @Test
    void createUser_DuplicateUserException() {
        // Given
        CreateUserForm form = new CreateUserForm();
        form.setUsername("duplicateUser");
        form.setPassword1("password123");
        form.setPassword2("password123");
        form.setEmail("duplicate@example.com");

        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(DataIntegrityViolationException.class).when(userService)
                .createUser(anyString(), anyString(), anyString());

        // When
        String result = userController.createUser(form, bindingResult);

        // Then
        assertEquals("signup_form", result);
        verify(bindingResult).reject("signupFailed", "이미 등록된 사용자 입니다.");
    }

    @Test
    void createUser_OtherException() {
        // Given
        CreateUserForm form = new CreateUserForm();
        form.setUsername("testUser");
        form.setPassword1("password123");
        form.setPassword2("password123");
        form.setEmail("test@example.com");

        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new RuntimeException("Unexpected Error")).when(userService)
                .createUser(anyString(), anyString(), anyString());

        // When
        String result = userController.createUser(form, bindingResult);

        // Then
        assertEquals("signup_form", result);
        verify(bindingResult).reject("signupFailed", "Unexpected Error");
    }
}
