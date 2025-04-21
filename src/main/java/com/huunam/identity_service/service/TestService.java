package com.huunam.identity_service.service;

import com.huunam.identity_service.dto.*;
import com.huunam.identity_service.entity.*;
import com.huunam.identity_service.exception.AppException;
import com.huunam.identity_service.exception.ErrorCode;
import com.huunam.identity_service.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final TestResultRepository testResultRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository; // Added UserRepository

    public TestDTO createTest(TestDTO testDTO) {
        if (testRepository.existsByTitle(testDTO.getTitle())) { // Check if the test title already exists
            throw new AppException(ErrorCode.TEST_ALREADY_EXISTS);
        }

        // Fetch the category by ID instead of name
        Category category = categoryRepository.findById(testDTO.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Create a new Test entity
        Test test = new Test();
        test.setTitle(testDTO.getTitle());
        test.setDescription(testDTO.getDescription());
        test.setTime(testDTO.getTime());
        test.setCategory(category);

        // Save the test and return its DTO
        return testRepository.save(test).toDto();
    }

    public QuestionDTO addQuestionInTest(QuestionDTO dto) {
        Test test = testRepository.findById(dto.getId())
                .orElseThrow(() -> new AppException(ErrorCode.TEST_NOT_FOUND));

        if (questionRepository.existsByQuestionTextAndTest(dto.getQuestionText(), test)) { // Updated method call
            throw new AppException(ErrorCode.QUESTION_ALREADY_EXISTS);
        }

        Question question = new Question();
        question.setTest(test);
        question.setQuestionText(dto.getQuestionText());
        question.setOptionA(dto.getOptionA());
        question.setOptionB(dto.getOptionB());
        question.setOptionC(dto.getOptionC());
        question.setOptionD(dto.getOptionD());
        question.setCorrectOption(dto.getCorrectOption());

        return questionRepository.save(question).toDto();
    }

    public TestDetailsDTO getAllQuestionsByTest(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEST_NOT_FOUND));

        return new TestDetailsDTO(
                test.toDto(),
                test.getQuestions().stream().map(Question::toDto).collect(Collectors.toList()));
    }

    public TestResultDTO submitTest(SubmitTestDTO request) {
        // Fetch the test by ID
        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new AppException(ErrorCode.TEST_NOT_FOUND));

        // Fetch the user by ID
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Calculate the test result
        int totalQuestions = test.getQuestions().size();
        int correctAnswers = (int) request.getResponses().stream()
                .filter(response -> {
                    Question question = questionRepository.findById(response.getQuestionId())
                            .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
                    return question.getCorrectOption().equals(response.getSelectedOption());
                })
                .count();
        double percentage = (double) correctAnswers / totalQuestions * 100;

        // Create and save the TestResult
        TestResult testResult = new TestResult();
        testResult.setTest(test);
        testResult.setUser(user); // Set the user
        testResult.setTotalQuestions(totalQuestions);
        testResult.setCorrectAnswers(correctAnswers);
        testResult.setPercentage(percentage);

        return testResultRepository.save(testResult).toDto();
    }

    public List<TestDTO> getAllTests() {
        return testRepository.findAll().stream()
                .map(Test::toDto)
                .collect(Collectors.toList());
    }

    public List<TestResultDTO> getAllTestResults() {
        return testResultRepository.findAll().stream()
                .map(TestResult::toDto)
                .collect(Collectors.toList());
    }

    public List<TestResultDTO> getAllTestResultsOfUser(String userId) {
        return testResultRepository.findAllByUserId(userId).stream()
                .map(TestResult::toDto)
                .collect(Collectors.toList());
    }

    public void deleteTest(Long testId) {
        if (!testRepository.existsById(testId)) {
            throw new RuntimeException("Test not found with id: " + testId);
        }
        testRepository.deleteById(testId);
    }

    public TestDTO updateTest(Long testId, TestDTO testDTO) {
        // Fetch the test by ID
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found with id: " + testId));

        // Update test details
        test.setTitle(testDTO.getTitle());
        test.setDescription(testDTO.getDescription());
        test.setTime(testDTO.getTime());

        // Fetch the category by ID instead of name
        Category category = categoryRepository.findById(testDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + testDTO.getCategoryId()));
        test.setCategory(category);

        // Save and return the updated test
        return testRepository.save(test).toDto();
    }

    public List<TestResultDTO> getTopPerformers(Long testId) {
        return testResultRepository.findTop5ByTestIdOrderByPercentageDesc(testId).stream()
                .map(TestResult::toDto)
                .collect(Collectors.toList());
    }
}