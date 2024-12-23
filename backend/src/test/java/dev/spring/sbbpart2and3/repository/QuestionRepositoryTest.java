package dev.spring.sbbpart2and3.repository;

import dev.spring.sbbpart2and3.domain.Question;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        em.createNativeQuery("ALTER TABLE QUESTION ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        for(int i = 1; i <= 50; i++) {
            questionRepository.save(new Question("테스트제목"+i, "테스트내용"+i, null));
        }
    }
    @Test
    public void saveQuestionTest() {

        assertEquals(50, questionRepository.count());
    }

    @Test
    public void findQuestionByIdTest() {
        // Given
        Optional<Question> q = questionRepository.findById(30L);

        // When
        assertTrue(q.isPresent());
        Question question = q.get();

        // Then
        assertEquals("테스트제목30", question.getSubject());
        assertEquals("테스트내용30", question.getContent());
    }

    @Test
    public void timeSetTest() {
        // Given
        Optional<Question> q = questionRepository.findById(1L);

        // When
        assertTrue(q.isPresent());
        Question question = q.get();

        // Then
        assertInstanceOf(LocalDateTime.class, question.getCreatedDate());
        assertInstanceOf(LocalDateTime.class, question.getLastModifiedDate());
    }
}
