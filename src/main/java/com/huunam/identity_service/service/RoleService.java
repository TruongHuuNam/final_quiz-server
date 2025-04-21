package com.huunam.identity_service.service;

import com.huunam.identity_service.dto.request.RoleRequest;
import com.huunam.identity_service.dto.response.RoleResponse;
import com.huunam.identity_service.mapper.RoleMapper;
import com.huunam.identity_service.repository.PermissionRepository;
import com.huunam.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j //inject Logger
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository; //need PermissionRepository to access in and get list permissions
    RoleMapper roleMapper;
    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());//id la cac role da truyen vao request
        role.setPermissions(new HashSet<>(permissions)); //tao hashset moi de luu tru list permissions

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        //var roles = roleRepository.findAll(); instead of this
        //return roles.stream().map(roleMapper::toRoleResponse).toList();
        //into this
        return roleRepository.findAll()
                .stream().map(roleMapper::toRoleResponse)
                .toList();//map to RoleResponse and change type to List
    }

    public List<RoleResponse> getRoleByName(String role){
        return roleRepository.findAllByName(role)
                .stream()
                .toList();
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
