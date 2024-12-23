package dev.spring.sbbpart2and3.controller;

import dev.spring.sbbpart2and3.form.CreateUserForm;
import dev.spring.sbbpart2and3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("signup")
    public String createUser(CreateUserForm createUserForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String createUser(@Valid CreateUserForm createUserForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }
        if(!createUserForm.getPassword1().equals(createUserForm.getPassword2())) {
            bindingResult.rejectValue("password2", "Passwords don't match",
                    "2개의 비밀번호가 일치하지 않습니다.");
            return "signup_form";
        }
        try{
            userService.createUser(createUserForm.getUsername(),
                    createUserForm.getPassword1(),
                    createUserForm.getEmail());
        } catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

}
