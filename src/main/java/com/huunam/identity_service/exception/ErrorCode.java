package com.huunam.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // General Errors
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error!", HttpStatus.INTERNAL_SERVER_ERROR), // HTTP 500
    INVALID_KEY(1001, "Invalid message key!", HttpStatus.BAD_REQUEST), // HTTP 400

    // User Errors
    USER_NOT_FOUND(1001, "User not found!", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User already exists!", HttpStatus.BAD_REQUEST), // HTTP 400
    USERNAME_INVALID(1003, "Username must be at least {min} characters!", HttpStatus.BAD_REQUEST), // HTTP 400
    PASSWORD_INVALID(1004, "Password must be at least {min} characters!", HttpStatus.BAD_REQUEST), // HTTP 400
    USER_NOT_EXISTED(1005, "User does not exist!", HttpStatus.NOT_FOUND), // HTTP 404
    USER_UNAUTHENTICATED(1006, "User unauthenticated!", HttpStatus.UNAUTHORIZED), // HTTP 401
    UNAUTHORIZED(1007, "You do not have permission!", HttpStatus.FORBIDDEN), // HTTP 403
    INVALID_DOB(1008, "Your age must be at least {min} years!", HttpStatus.BAD_REQUEST), // HTTP 400

    // Category Errors
    CATEGORY_NOT_FOUND(2001, "Category not found!", HttpStatus.NOT_FOUND), // HTTP 404
    CATEGORY_ALREADY_EXISTS(2002, "Category already exists!", HttpStatus.BAD_REQUEST), // HTTP 400

    // Test Errors
    TEST_NOT_FOUND(3001, "Test not found!", HttpStatus.NOT_FOUND), // HTTP 404
    TEST_ALREADY_EXISTS(3002, "Test already exists!", HttpStatus.BAD_REQUEST), // HTTP 400
    TEST_SUBMISSION_FAILED(3003, "Test submission failed!", HttpStatus.BAD_REQUEST), // HTTP 400

    // Question Errors
    QUESTION_NOT_FOUND(4001, "Question not found!", HttpStatus.NOT_FOUND), // HTTP 404
    QUESTION_ALREADY_EXISTS(4002, "Question already exists!", HttpStatus.BAD_REQUEST), // HTTP 400
    INVALID_QUESTION_FORMAT(4003, "Invalid question format!", HttpStatus.BAD_REQUEST), // HTTP 400

    // Authentication and Authorization Errors
    TOKEN_EXPIRED(5001, "Token has expired!", HttpStatus.UNAUTHORIZED), // HTTP 401
    TOKEN_INVALID(5002, "Invalid token!", HttpStatus.UNAUTHORIZED), // HTTP 401

    // Validation Errors
    INVALID_INPUT(6001, "Invalid input provided!", HttpStatus.BAD_REQUEST), // HTTP 400
    MISSING_REQUIRED_FIELDS(6002, "Missing required fields!", HttpStatus.BAD_REQUEST), // HTTP 400

    // File Upload Errors
    FILE_UPLOAD_FAILED(7001, "File upload failed!", HttpStatus.INTERNAL_SERVER_ERROR), // HTTP 500
    FILE_NOT_FOUND(7002, "File not found!", HttpStatus.NOT_FOUND), // HTTP 404

    // Database Errors
    DATABASE_ERROR(8001, "Database error occurred!", HttpStatus.INTERNAL_SERVER_ERROR), // HTTP 500
    DUPLICATE_ENTRY(8002, "Duplicate entry in the database!", HttpStatus.BAD_REQUEST); // HTTP 400

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}