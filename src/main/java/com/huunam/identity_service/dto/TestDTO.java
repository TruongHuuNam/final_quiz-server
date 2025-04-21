package com.huunam.identity_service.dto;

import lombok.Data;

@Data
public class TestDTO {
    private Long id;
    private String title;
    private String description;
    private Long time;
    private Long categoryId;
}