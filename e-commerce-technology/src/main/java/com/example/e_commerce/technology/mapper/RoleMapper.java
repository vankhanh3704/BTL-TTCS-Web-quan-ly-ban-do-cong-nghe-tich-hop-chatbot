package com.example.e_commerce.technology.mapper;


import com.example.e_commerce.technology.Entity.RoleEntity;
import com.example.e_commerce.technology.model.request.RoleRequest;
import com.example.e_commerce.technology.model.response.RoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleEntity toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(RoleEntity role);
}
