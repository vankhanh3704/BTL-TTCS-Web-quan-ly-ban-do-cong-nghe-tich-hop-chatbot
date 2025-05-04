package com.example.e_commerce.technology.service.impl;



import com.example.e_commerce.technology.mapper.RoleMapper;
import com.example.e_commerce.technology.model.request.RoleRequest;
import com.example.e_commerce.technology.model.response.RoleResponse;
import com.example.e_commerce.technology.repository.RoleRepository;
import com.example.e_commerce.technology.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;


    public RoleResponse createRole(RoleRequest roleRequest) {
        var role = roleMapper.toRole(roleRequest);
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }


}
