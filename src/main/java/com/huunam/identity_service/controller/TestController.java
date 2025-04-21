package com.huunam.identity_service.controller;

import com.huunam.identity_service.dto.*;
import com.huunam.identity_service.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Tag(name = "Test API", description = "APIs for managing tests and test results")
public class TestController {
    private final TestService testService;

    @PostMapping
    @Operation(summary = "Create a new test", description = "Creates a new test with the provided details.")
    public TestDTO createTest(@RequestBody TestDTO testDTO) {
        return testService.createTest(testDTO);
    }

    @PostMapping("/{testId}/questions")
    @Operation(summary = "Add a question to a test", description = "Adds a new question to the specified test.")
    public QuestionDTO addQuestionInTest(@PathVariable Long testId, @RequestBody QuestionDTO questionDTO) {
        questionDTO.setId(testId);
        return testService.addQuestionInTest(questionDTO);
    }

    @GetMapping
    @Operation(summary = "Get all tests", description = "Retrieves a list of all tests.")
    public List<TestDTO> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/{testId}/questions")
    @Operation(summary = "Get all questions for a test", description = "Retrieves all questions for the specified test.")
    public TestDetailsDTO getAllQuestionsByTest(@PathVariable Long testId) {
        return testService.getAllQuestionsByTest(testId);
    }

    @PostMapping("/submit")
    @Operation(summary = "Submit a test", description = "Submits a test and calculates the result.")
    public TestResultDTO submitTest(@RequestBody SubmitTestDTO request) {
        return testService.submitTest(request);
    }

    @GetMapping("/results")
    @Operation(summary = "Get all test results", description = "Retrieves all test results.")
    public List<TestResultDTO> getAllTestResults() {
        return testService.getAllTestResults();
    }

    @GetMapping("/results/user/{userId}")
    @Operation(summary = "Get test results for a user", description = "Retrieves all test results for the specified user.")
    public List<TestResultDTO> getAllTestResultsOfUser(@PathVariable String userId) {
        return testService.getAllTestResultsOfUser(userId);
    }

    @DeleteMapping("/{testId}")
    @Operation(summary = "Delete a test", description = "Deletes the specified test.")
    public void deleteTest(@PathVariable Long testId) {
        testService.deleteTest(testId);
    }

    @PutMapping("/{testId}")
    @Operation(summary = "Update a test", description = "Updates the details of the specified test.")
    public TestDTO updateTest(@PathVariable Long testId, @RequestBody TestDTO testDTO) {
        return testService.updateTest(testId, testDTO);
    }

    @GetMapping("/{testId}/top-performers")
    @Operation(summary = "Get top performers for a test", description = "Retrieves the top 5 performers for the specified test.")
    public List<TestResultDTO> getTopPerformers(@PathVariable Long testId) {
        return testService.getTopPerformers(testId);
    }
}