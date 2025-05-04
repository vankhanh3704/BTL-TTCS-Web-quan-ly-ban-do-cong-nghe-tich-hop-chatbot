package com.example.e_commerce.technology.service;


import com.example.e_commerce.technology.model.request.UserCreationRequest;
import com.example.e_commerce.technology.model.request.UserUpdateRequest;
import com.example.e_commerce.technology.model.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    UserResponse createUser(UserCreationRequest user);
    Page<UserResponse> getUsers(Pageable pageable);
    UserResponse getUserById(Long id);
    UserResponse updateUser(Long id, UserUpdateRequest user);
    void deleteUserById(Long id);
    UserResponse getMyInfo();
    Page<UserResponse> searchUsersByKeyword(String keyword, Pageable pageable);
}
