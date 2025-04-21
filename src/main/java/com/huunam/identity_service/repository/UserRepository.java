package com.huunam.identity_service.repository;

import com.huunam.identity_service.entity.Role;
import com.huunam.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // tao bean UserRepository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findByRolesContaining(Role role); // Corrected method to search by roles

    User findFirstByEmail(String email);

    Optional<User> findByEmail(String email);

    // Corrected method to search by firstName or email
    List<User> findByFirstNameContainingOrEmailContaining(String firstName, String email);
}