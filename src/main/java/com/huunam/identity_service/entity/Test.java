package com.huunam.identity_service.entity;

import com.huunam.identity_service.dto.TestDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Long time;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    private List<Question> questions;

    public TestDTO toDto() {
        TestDTO dto = new TestDTO();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setTime(time);
        dto.setCategoryId(category != null ? category.getId() : null);
        return dto;
    }
}