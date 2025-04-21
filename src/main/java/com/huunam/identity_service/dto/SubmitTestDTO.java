package com.huunam.identity_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubmitTestDTO {
    private Long testId;
    private String userId;
    private List<QuestionResponseDTO> responses;
}