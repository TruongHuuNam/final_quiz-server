package com.huunam.identity_service.mapper;

import com.huunam.identity_service.dto.request.PermissionRequest;
import com.huunam.identity_service.dto.request.UserCreationRequest;
import com.huunam.identity_service.dto.request.UserUpdateRequest;
import com.huunam.identity_service.dto.response.PermissionResponse;
import com.huunam.identity_service.dto.response.UserResponse;
import com.huunam.identity_service.entity.Permission;
import com.huunam.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") //bao cho mapper mapstruct biet la ta se generate mapper de su dung trong spring (dependency injection)
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request); //nhan vao mot params la request va tra ve class Permission
    PermissionResponse toPermissionResponse(Permission permission);
}
