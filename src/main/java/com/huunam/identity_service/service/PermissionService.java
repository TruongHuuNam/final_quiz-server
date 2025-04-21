package com.huunam.identity_service.service;

import com.huunam.identity_service.dto.request.PermissionRequest;
import com.huunam.identity_service.dto.response.PermissionResponse;
import com.huunam.identity_service.entity.Permission;
import com.huunam.identity_service.mapper.PermissionMapper;
import com.huunam.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j //inject Logger
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);//map permission from request
        permission = permissionRepository.save(permission); //save to database
        return permissionMapper.toPermissionResponse(permission); //return object permission response
    }

    public List<PermissionResponse> getAllPermission(){
        var permissions = permissionRepository.findAll(); //get all permission in DB
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList(); //return by mapper tolist
    }

    public void deletePermission(String permission){
        permissionRepository.deleteById(permission);
    }
}
