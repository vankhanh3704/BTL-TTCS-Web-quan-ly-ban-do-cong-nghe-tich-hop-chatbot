package com.example.e_commerce.technology.service;


import com.example.e_commerce.technology.model.request.RoleRequest;
import com.example.e_commerce.technology.model.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    List<RoleResponse> getAllRoles();
    void deleteRole(String id);
}
