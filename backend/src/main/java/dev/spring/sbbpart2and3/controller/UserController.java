package dev.spring.sbbpart2and3.controller;

import dev.spring.sbbpart2and3.dto.UserDTO;
import dev.spring.sbbpart2and3.form.CreateUserForm;
import dev.spring.sbbpart2and3.form.LoginForm;
import dev.spring.sbbpart2and3.service.UserSecurityService;
import dev.spring.sbbpart2and3.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserSecurityService userSecurityService;
    @Autowired
    public UserController(UserService userService, UserSecurityService userSecurityService) {
        this.userService = userService;
        this.userSecurityService = userSecurityService;
    }

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

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginForm loginForm, HttpSession session) {
        try {
            boolean isAuthenticated = userService.authenticate(loginForm.getUsername(), loginForm.getPassword());
            if (isAuthenticated) {
                session.setAttribute("user", loginForm.getUsername());
                return ResponseEntity.ok("로그인 성공");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 잘못된 사용자 이름 또는 비밀번호");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session) {
        try {
            session.invalidate(); // 세션 종료
            return ResponseEntity.ok("로그아웃 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Object> getUserInfo(HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인되지 않은 사용자입니다.");
        }

        try {
            // UserDetailsService를 통해 UserDetails 객체를 가져오고
            UserDetails userDetails = userSecurityService.loadUserByUsername(username);

            // UserDTO로 변환하여 클라이언트에 응답
            UserDTO userDTO = new UserDTO(userDetails.getUsername());
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
