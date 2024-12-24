package dev.spring.sbbpart2and3.controller;

import dev.spring.sbbpart2and3.domain.Answer;
import dev.spring.sbbpart2and3.domain.Question;
import dev.spring.sbbpart2and3.domain.SiteUser;
import dev.spring.sbbpart2and3.dto.AnswerDTO;
import dev.spring.sbbpart2and3.form.AnswerForm;
import dev.spring.sbbpart2and3.service.AnswerService;
import dev.spring.sbbpart2and3.service.QuestionService;
import dev.spring.sbbpart2and3.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/answers")
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;

    @Autowired
    public AnswerController(AnswerService answerService, QuestionService questionService, UserService userService) {
        this.answerService = answerService;
        this.questionService = questionService;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public ResponseEntity<AnswerDTO> createAnswer(@PathVariable("id") Long questionId,
                         @Valid AnswerForm answerForm, BindingResult bindingResult,
                         Principal principal) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Question question = questionService.getQuestionById(questionId);
        SiteUser siteUser = userService.findUserByUsername(principal.getName());
        Answer answer = new Answer(answerForm.getContent(), siteUser);

        question.addAnswer(answer);
        AnswerDTO answerDTO = answerService.save(answer);
        return ResponseEntity.created(URI.create("redirect:/question/detail/" + questionId + "#answer_" + answerDTO.id()))
                .body(answerDTO);
    }

    /*@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public ResponseEntity<AnswerForm> modifyAnswer(@PathVariable("id") Long id, AnswerForm answerForm, Principal principal) {
        AnswerDTO answerDTO = answerService.getAnswerDTOById(id);
        if(!answerDTO.author().equals(principal.getName())) {
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        answerForm.setContent(answerDTO.content());
        return ResponseEntity.ok(answerForm);
    }*/

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public ResponseEntity<Long> modifyAnswer(@PathVariable("id") Long id, Principal principal,
            @Valid AnswerForm answerForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        AnswerDTO answerDTO = answerService.getAnswerDTOById(id);
        if(!answerDTO.author().equals(principal.getName())) {
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        answerService.modify(id, answerForm.getContent());
        return ResponseEntity.ok(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable("id") Long id, AnswerForm answerForm, Principal principal) {
        AnswerDTO answerDTO = answerService.getAnswerDTOById(id);
        if(!answerDTO.author().equals(principal.getName())) {
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        answerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public ResponseEntity<Long> voteAnswer(@PathVariable("id") Long id, Principal principal,
                             @RequestParam("questionId") Long questionId) {
        AnswerDTO answerDTO = answerService.vote(id, principal.getName());
        return ResponseEntity.ok(id);
    }
}
