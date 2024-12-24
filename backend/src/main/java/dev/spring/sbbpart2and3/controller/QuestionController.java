package dev.spring.sbbpart2and3.controller;

import dev.spring.sbbpart2and3.domain.SiteUser;
import dev.spring.sbbpart2and3.dto.AnswerDTO;
import dev.spring.sbbpart2and3.dto.QuestionDTO;
import dev.spring.sbbpart2and3.dto.QuestionDetailDTO;
import dev.spring.sbbpart2and3.dto.QuestionListDTO;
import dev.spring.sbbpart2and3.form.AnswerForm;
import dev.spring.sbbpart2and3.form.QuestionForm;
import dev.spring.sbbpart2and3.service.AnswerService;
import dev.spring.sbbpart2and3.service.QuestionService;
import dev.spring.sbbpart2and3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;
    @Autowired
    public QuestionController(QuestionService questionService,
                              AnswerService answerService,
                              UserService userService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<Page<QuestionListDTO>> getQuestionList(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "kw", defaultValue = "") String kw, Model model) {
        Page<QuestionListDTO> questionList = questionService.getPagedQuestionDTOs(kw, page);
        return ResponseEntity.ok(questionList);
    }

/*    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createQuestion(QuestionForm questionForm) {
        return "question_form";
    }*/

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<Void> createQuestion(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        SiteUser siteUser = userService.findUserByUsername(principal.getName());

        questionService.save(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<QuestionDetailDTO> getQuestionDetail(@PathVariable("id") Long id, AnswerForm answerForm, Model model,
                                                               @RequestParam(value = "page", defaultValue = "0") int page) {
        QuestionDTO questionDTO = questionService.getQuestionDTOById(id);
        Page<AnswerDTO> answerList = answerService.getPagedAnswerDTOs(id, page);

        return ResponseEntity.ok(new QuestionDetailDTO(questionDTO, answerList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public ResponseEntity<QuestionForm> modifyQuestion(@PathVariable("id") Long id, QuestionForm questionForm, Principal principal) {
        QuestionDTO questionDTO = questionService.getQuestionDTOById(id);
        if(!questionDTO.author().equals(principal.getName())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(questionForm);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("modify/{id}")
    public ResponseEntity<Void> modifyQuestion(@PathVariable("id") Long id, Principal principal,
                               @Valid QuestionForm questionForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        QuestionDTO question = questionService.getQuestionDTOById(id);
        if(!question.author().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        questionService.modify(id, questionForm.getSubject(), questionForm.getContent());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("id") Long id, Principal principal) {
        QuestionDTO questionDTO = questionService.getQuestionDTOById(id);
        if(!questionDTO.author().equals(principal.getName())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        questionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public ResponseEntity<Void> voteQuestion(@PathVariable("id") Long id, Principal principal) {
        questionService.vote(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
