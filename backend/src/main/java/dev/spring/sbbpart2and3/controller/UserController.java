package dev.spring.sbbpart2and3.controller;

import dev.spring.sbbpart2and3.form.CreateUserForm;
import dev.spring.sbbpart2and3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*@GetMapping("signup")
    public String createUser(CreateUserForm createUserForm) {
        return "signup_form";
    }*/

    @PostMapping("/signup")
    public ResponseEntity<Object> createUser(@Valid CreateUserForm createUserForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        if(!createUserForm.getPassword1().equals(createUserForm.getPassword2())) {
           return ResponseEntity.badRequest().body("2개의 비밀번호가 일치하지 않습니다.");
        }
        try{
            userService.createUser(createUserForm.getUsername(),
                    createUserForm.getPassword1(),
                    createUserForm.getEmail());
        } catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 사용자입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    /*@GetMapping("/login")
    public String login() {
        return "login_form";
    }*/
}
