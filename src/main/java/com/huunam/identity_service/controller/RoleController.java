package com.huunam.identity_service.controller;

import com.huunam.identity_service.dto.request.ApiResponse;
import com.huunam.identity_service.dto.request.RoleRequest;
import com.huunam.identity_service.dto.response.RoleResponse;
import com.huunam.identity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createPermission(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRole() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @GetMapping("/{role}")
    ApiResponse<List<RoleResponse>> getRoleById(@PathVariable String role) {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getRoleByName(role))
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> deletePermission(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder().build();
    }
}
