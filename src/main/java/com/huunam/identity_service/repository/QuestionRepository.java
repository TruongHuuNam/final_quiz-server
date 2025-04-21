package com.huunam.identity_service.repository;

import com.huunam.identity_service.entity.Question;
import com.huunam.identity_service.entity.Test;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    boolean existsByQuestionTextAndTest(String questionText, Test test);
}