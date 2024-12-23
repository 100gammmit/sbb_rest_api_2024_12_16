package dev.spring.sbbpart2and3.controller.admin;

import dev.spring.sbbpart2and3.dto.AnswerDTO;
import dev.spring.sbbpart2and3.form.AnswerForm;
import dev.spring.sbbpart2and3.service.AnswerService;
import dev.spring.sbbpart2and3.service.QuestionService;
import dev.spring.sbbpart2and3.service.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/manage")
public class AdminManageController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserSecurityService userSecurityService;

    @Autowired
    public AdminManageController(QuestionService questionService,
                                 AnswerService answerService,
                                 UserSecurityService userSecurityService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.userSecurityService = userSecurityService;
    }

    @GetMapping("/role")
    public String manageRole(Model model) {
        List<UserDetails> userDetails = userSecurityService.loadAllUser();
        model.addAttribute("users", userDetails);
        return "admin_manage_role";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/question/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id) {
        questionService.delete(id);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/answer/delete/{id}")
    public String deleteAnswer(@PathVariable("id") Long id, AnswerForm answerForm) {
        AnswerDTO answerDTO = answerService.getAnswerDTOById(id);
        answerService.delete(id);
        return "redirect:/question/detail/" + answerDTO.questionId();
    }
}
