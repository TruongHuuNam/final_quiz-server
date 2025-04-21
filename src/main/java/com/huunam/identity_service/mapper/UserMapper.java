package com.huunam.identity_service.mapper;

import com.huunam.identity_service.dto.request.UserCreationRequest;
import com.huunam.identity_service.dto.request.UserUpdateRequest;
import com.huunam.identity_service.dto.response.UserResponse;
import com.huunam.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") //bao cho mapper mapstruct biet la ta se generate mapper de su dung trong spring (dependency injection)
public interface UserMapper {
    User toUser(UserCreationRequest request); //nhan vao mot params la request va tra ve class User
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);//@MappingTarget: define that map data from UserUpdateRequest into Object User
}
