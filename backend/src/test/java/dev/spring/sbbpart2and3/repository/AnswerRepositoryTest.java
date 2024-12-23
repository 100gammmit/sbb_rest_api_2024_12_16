package dev.spring.sbbpart2and3.repository;

import dev.spring.sbbpart2and3.domain.Answer;
import dev.spring.sbbpart2and3.domain.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class AnswerRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        em.createNativeQuery("ALTER TABLE QUESTION ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE ANSWER ALTER COLUMN ID RESTART WITH 1").executeUpdate();

        questionRepository.save(new Question("테스트제목1", "테스트내용1", null));
        questionRepository.save(new Question("테스트제목2", "테스트내용2", null));
        Question q1 = questionRepository.findById(1L).get();
        Question q2 = questionRepository.findById(2L).get();

        Answer a1 = new Answer("답변1", null);
        Answer a2 = new Answer("답변2", null);
        Answer a3 = new Answer("답변3", null);
        q1.addAnswer(a1);
        q2.addAnswer(a2);
        q2.addAnswer(a3);
        
        answerRepository.save(a1);
        answerRepository.save(a2);
        answerRepository.save(a3);
    }

    @Test
    public void answerSaveTest() {
        assertEquals(3, answerRepository.count());
        assertEquals(1, questionRepository.findById(1L).get().getAnswerList().size());
        assertEquals(2, questionRepository.findById(2L).get().getAnswerList().size());
    }

    @Test
    public void findByIdTest() {
        // Given
        Optional<Answer> a = answerRepository.findById(1L);
        Assertions.assertTrue(a.isPresent());
        // When
        Answer answer = a.get();
        // Then
        assertEquals("답변1", answer.getContent());
    }

}
