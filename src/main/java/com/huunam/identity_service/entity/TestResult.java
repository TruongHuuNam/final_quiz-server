package com.huunam.identity_service.entity;

import com.huunam.identity_service.dto.TestResultDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    int totalQuestions;
    int correctAnswers;
    double percentage;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    public TestResultDTO toDto() {
        TestResultDTO dto = new TestResultDTO();
        dto.setId(id);
        dto.setTotalQuestions(totalQuestions);
        dto.setCorrectAnswers(correctAnswers);
        dto.setPercentage(percentage);
        dto.setTestName(test.getTitle());
        dto.setUserName(user.getUsername());
        return dto;
    }
}