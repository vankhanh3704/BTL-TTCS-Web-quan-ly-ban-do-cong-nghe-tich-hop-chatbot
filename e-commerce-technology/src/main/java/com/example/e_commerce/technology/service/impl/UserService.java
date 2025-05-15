package com.example.e_commerce.technology.service.impl;



import com.example.e_commerce.technology.Entity.RoleEntity;
import com.example.e_commerce.technology.Entity.UserEntity;
import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.constant.PredefinedRole;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.mapper.UserMapper;
import com.example.e_commerce.technology.model.request.UserCreationRequest;
import com.example.e_commerce.technology.model.request.UserInfoUpdateRequest;
import com.example.e_commerce.technology.model.request.UserUpdateRequest;
import com.example.e_commerce.technology.model.response.UserResponse;
import com.example.e_commerce.technology.repository.RoleRepository;
import com.example.e_commerce.technology.repository.UserRepository;
import com.example.e_commerce.technology.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;


    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        UserEntity user = userMapper.toUser(request);
        // mã hoá
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        HashSet<RoleEntity> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return userMapper.toUserResponse(user);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public Page<UserResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserResponse);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public UserResponse getUserById(Long id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("User not found")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getMyInfo(){
        UserEntity user = getCurrentUser();
        return userMapper.toUserResponse(user);
    }


    @Override
    public UserResponse updateMyInfo(UserInfoUpdateRequest request) {
        UserEntity user = getCurrentUser();
        userMapper.updateUserInfo(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }


    public Page<UserResponse> searchUsersByKeyword(String keyword, Pageable pageable) {

        Page<UserEntity> users = userRepository.searchUsersByKeyword(keyword, pageable);
        return users.map(userMapper::toUserResponse);
    }

    private UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
