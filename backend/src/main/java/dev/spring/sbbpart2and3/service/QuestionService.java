package dev.spring.sbbpart2and3.service;

import dev.spring.sbbpart2and3.domain.Question;
import dev.spring.sbbpart2and3.domain.SiteUser;
import dev.spring.sbbpart2and3.dto.QuestionDTO;
import dev.spring.sbbpart2and3.dto.QuestionListDTO;
import dev.spring.sbbpart2and3.exception.QuestionNotFoundException;
import dev.spring.sbbpart2and3.repository.AnswerRepository;
import dev.spring.sbbpart2and3.repository.QuestionRepository;
import dev.spring.sbbpart2and3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static dev.spring.sbbpart2and3.dto.QuestionDTO.toQuestionDto;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           AnswerRepository answerRepository,
                           UserRepository userRepository
                           ) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public void save(String subject, String content, SiteUser siteUser) {
        questionRepository.save(new Question(subject, content, siteUser));
    }

    public void modify(Long id, String subject, String content) {
        Question question = questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
        question.setSubject(subject);
        question.setContent(content);
        questionRepository.save(question);
    }

    public void delete(Long id) {
        questionRepository.deleteById(id);
    }

    public QuestionDTO getQuestionDTOById(Long id) {
        return questionRepository.findByIdToDto(id).orElseThrow(QuestionNotFoundException::new);
    }

    public Question getQuestionById(Long id) {
        return  questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    public Page<QuestionListDTO> getPagedQuestionDTOs(String kw, int page) {
        /*Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, 20, sort);
        Page<Question> questions = questionRepository.findAllByKeyword(kw, pageable);
        return questions.map(QuestionListDTO::toQuestionListDTO);*/
        Pageable pageable = PageRequest.of(page, 20);
        return questionRepository.findAllByKeyword(kw, pageable);
    }

    public void vote(Long questionId, String username) {
        Question question = getQuestionById(questionId);
        SiteUser siteUser = userRepository.findByUsername(username).orElseThrow(QuestionNotFoundException::new);
        question.addVoter(siteUser);
        questionRepository.save(question);
    }

}
