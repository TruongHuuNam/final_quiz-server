package com.huunam.identity_service.repository;

import com.huunam.identity_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name); 

    Optional<Category> findByName(String name);
}