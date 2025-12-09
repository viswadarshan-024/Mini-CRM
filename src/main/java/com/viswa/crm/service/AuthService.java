package com.viswa.crm.service;

import com.viswa.crm.dto.auth.CreateUserRequest;
import com.viswa.crm.dto.auth.LoginRequest;
import com.viswa.crm.dto.auth.LoginResponse;
import com.viswa.crm.dto.auth.UserResponse;

import java.util.List;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void logout(Long userId);

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(Long userId);

    List<UserResponse> getAllUsers();
}
