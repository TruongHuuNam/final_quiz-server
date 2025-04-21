package com.huunam.identity_service.dto;

import lombok.Data;

@Data
public class QuestionResponseDTO {
    private Long questionId;
    private String selectedOption;
}