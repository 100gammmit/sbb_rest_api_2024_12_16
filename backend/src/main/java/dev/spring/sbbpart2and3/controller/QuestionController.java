package dev.spring.sbbpart2and3.controller;

import dev.spring.sbbpart2and3.domain.SiteUser;
import dev.spring.sbbpart2and3.dto.AnswerDTO;
import dev.spring.sbbpart2and3.dto.QuestionDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping("/question")
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
    public String getQuestionList(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "kw", defaultValue = "") String kw, Model model) {
        Page<QuestionListDTO> questionList = questionService.getPagedQuestionDTOs(kw, page);
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createQuestion(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createQuestion(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = userService.findUserByUsername(principal.getName());
        questionService.save(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }

    @GetMapping("/detail/{id}")
    public String getQuestionDetail(@PathVariable("id") Long id, AnswerForm answerForm, Model model,
                                    @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<AnswerDTO> answerList = answerService.getPagedAnswerDTOs(id, page);
        model.addAttribute("question", questionService.getQuestionDTOById(id));
        model.addAttribute("answerList", answerList);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyQuestion(@PathVariable("id") Long id, QuestionForm questionForm, Principal principal) {
        QuestionDTO questionDTO = questionService.getQuestionDTOById(id);
        if(!questionDTO.author().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("modify/{id}")
    public String modifyQuestion(@PathVariable("id") Long id, Principal principal,
                               @Valid QuestionForm questionForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "answer_form";
        }
        QuestionDTO question = questionService.getQuestionDTOById(id);
        if(!question.author().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        questionService.modify(id, questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/detail/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id, Principal principal) {
        QuestionDTO questionDTO = questionService.getQuestionDTOById(id);
        if(!questionDTO.author().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        questionService.delete(id);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String voteQuestion(@PathVariable("id") Long id, Principal principal) {
        questionService.vote(id, principal.getName());
        return "redirect:/question/detail/" + id;
    }
}
