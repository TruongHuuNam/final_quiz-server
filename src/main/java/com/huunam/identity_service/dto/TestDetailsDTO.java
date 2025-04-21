package com.huunam.identity_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDetailsDTO {
    private TestDTO test;
    private List<QuestionDTO> questions;
}