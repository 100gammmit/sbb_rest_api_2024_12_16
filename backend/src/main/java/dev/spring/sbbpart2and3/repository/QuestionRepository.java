package dev.spring.sbbpart2and3.repository;

import dev.spring.sbbpart2and3.domain.Question;
import dev.spring.sbbpart2and3.dto.QuestionDTO;
import dev.spring.sbbpart2and3.dto.QuestionListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findAll(Pageable pageable);

    @Query("SELECT new dev.spring.sbbpart2and3.dto.QuestionListDTO(q.id, q.subject, q.createdDate, " +
            "SIZE(q.answerList), COALESCE(q.author.username, null)) " +
            "FROM Question q " +
            "LEFT JOIN q.author a " +
            "WHERE q.subject LIKE %:kw% OR q.content LIKE %:kw% " +
            "ORDER BY q.createdDate DESC")
    Page<QuestionListDTO> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    @Query("SELECT new dev.spring.sbbpart2and3.dto.QuestionDTO(q.id, q.subject, q.content, " +
            "q.author.username, SIZE(q.voter), q.createdDate, q.lastModifiedDate) " +
            "FROM Question q " +
            "LEFT JOIN q.author " +
            "WHERE q.id = :id")
    Optional<QuestionDTO> findByIdToDto(@Param("id") Long id);
}
