package com.huunam.identity_service.mapper;

import com.huunam.identity_service.dto.request.PermissionRequest;
import com.huunam.identity_service.dto.request.RoleRequest;
import com.huunam.identity_service.dto.response.PermissionResponse;
import com.huunam.identity_service.dto.response.RoleResponse;
import com.huunam.identity_service.entity.Permission;
import com.huunam.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") //bao cho mapper mapstruct biet la ta se generate mapper de su dung trong spring (dependency injection)
public interface RoleMapper {
    @Mapping(target = "permissions",ignore = true)
    Role toRole(RoleRequest request); //nhan vao mot params la request va tra ve class Permission
    RoleResponse toRoleResponse(Role role);
}
