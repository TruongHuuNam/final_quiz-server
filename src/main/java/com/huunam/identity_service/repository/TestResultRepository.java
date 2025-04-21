package com.huunam.identity_service.repository;

import com.huunam.identity_service.entity.TestResult;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findAllByUserId(String userId);

    List<TestResult> findTop5ByTestIdOrderByPercentageDesc(Long testId);
}