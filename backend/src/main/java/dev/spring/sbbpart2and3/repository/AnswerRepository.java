package dev.spring.sbbpart2and3.repository;

import dev.spring.sbbpart2and3.domain.Answer;
import dev.spring.sbbpart2and3.dto.AnswerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {
    /*@Query("SELECT a FROM Answer a WHERE a.question.id = :questionId")
    Page<Answer> findByQuestionId(@Param("questionId") Long questionId, Pageable pageable);*/

    @Query("SELECT new dev.spring.sbbpart2and3.dto.AnswerDTO(a.id, a.content, " +
            "a.author.username, a.question.id, SIZE(a.voter), a.createdDate, a.lastModifiedDate) " +
            "FROM Answer a LEFT JOIN a.author " +
            "WHERE a.question.id = :questionId " +
            "order by a.createdDate desc ")
    Page<AnswerDTO> findByQuestionId(@Param("questionId") Long questionId, Pageable pageable);
}
