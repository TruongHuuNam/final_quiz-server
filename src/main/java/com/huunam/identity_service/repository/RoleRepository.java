package com.huunam.identity_service.repository;

import com.huunam.identity_service.dto.response.RoleResponse;
import com.huunam.identity_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    List<RoleResponse> findAllByName(String role);
}
