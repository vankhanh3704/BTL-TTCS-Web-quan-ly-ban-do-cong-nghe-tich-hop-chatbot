package com.example.e_commerce.technology.mapper;

import com.example.e_commerce.technology.Entity.UserEntity;
import com.example.e_commerce.technology.model.request.UserCreationRequest;
import com.example.e_commerce.technology.model.request.UserInfoUpdateRequest;
import com.example.e_commerce.technology.model.request.UserUpdateRequest;
import com.example.e_commerce.technology.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUser(UserCreationRequest userCreationRequest);
    @Mapping(target = "roles", source = "roles")
    UserResponse toUserResponse(UserEntity userEntity);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget UserEntity userEntity, UserUpdateRequest request);

    @Mapping(target = "roles", ignore = true)
    void updateUserInfo(@MappingTarget UserEntity userEntity, UserInfoUpdateRequest request);
}
