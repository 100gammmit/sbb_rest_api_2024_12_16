package dev.spring.sbbpart2and3.service;

import dev.spring.sbbpart2and3.domain.Answer;
import dev.spring.sbbpart2and3.domain.SiteUser;
import dev.spring.sbbpart2and3.dto.AnswerDTO;
import dev.spring.sbbpart2and3.exception.AnswerNotFoundException;
import dev.spring.sbbpart2and3.exception.UserNotFoundException;
import dev.spring.sbbpart2and3.repository.AnswerRepository;
import dev.spring.sbbpart2and3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static dev.spring.sbbpart2and3.dto.AnswerDTO.toAnswerDto;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    @Autowired
    public AnswerService(AnswerRepository answerRepository, UserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    public AnswerDTO save(Answer answer) {
        answerRepository.save(answer);
        return toAnswerDto(answer);
    }

    public void modify(Long id, String content) {
        Answer answer = answerRepository.findById(id).orElseThrow(AnswerNotFoundException::new);
        answer.setContent(content);
        answerRepository.save(answer);
    }

    public void delete(Long id) {
        answerRepository.deleteById(id);
    }

    public AnswerDTO getAnswerDTOById(Long id) {
        Answer answer = answerRepository.findById(id).orElseThrow(AnswerNotFoundException::new);
        return toAnswerDto(answer);
    }

    public Page<AnswerDTO> getPagedAnswerDTOs(Long questionId, int page) {
        /*Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Answer> answers = answerRepository.findByQuestionId(questionId, pageable);
        return answers.map(AnswerDTO::toAnswerDto);*/
        return answerRepository.findByQuestionId(questionId, PageRequest.of(page, 10));
    }

    public AnswerDTO vote(Long answerId, String username) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(AnswerNotFoundException::new);
        SiteUser siteUser = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        answer.addVoter(siteUser);
        answerRepository.save(answer);
        return toAnswerDto(answer);
    }

}
