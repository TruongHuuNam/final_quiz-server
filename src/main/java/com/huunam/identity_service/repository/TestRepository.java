package com.huunam.identity_service.repository;

import com.huunam.identity_service.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
    boolean existsByTitle(String title);
}