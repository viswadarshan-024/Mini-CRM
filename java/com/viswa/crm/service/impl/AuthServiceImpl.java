package com.viswa.crm.service.impl;

import com.viswa.crm.dto.auth.CreateUserRequest;
import com.viswa.crm.dto.auth.LoginRequest;
import com.viswa.crm.dto.auth.LoginResponse;
import com.viswa.crm.dto.auth.UserResponse;
import com.viswa.crm.model.Role;
import com.viswa.crm.model.User;
import com.viswa.crm.repository.DealRepository;
import com.viswa.crm.repository.RoleRepository;
import com.viswa.crm.repository.UserRepository;
import com.viswa.crm.service.AuthService;
import com.viswa.crm.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DealRepository dealRepository;

    @Override
    @Transactional(readOnly = true)  // Making read only for DB in the case of login
    public LoginResponse login(LoginRequest request) {

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userOpt.get();

        if (!PasswordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        return mapToLoginResponse(user);
    }

    @Override
    public void logout(Long userId) {
        // No logic, since session handling is done in controller
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        // Check if username already exists
        userRepository.findByUsername(request.getUsername()).ifPresent(u -> {
            throw new RuntimeException("Username already exists");
        });

        Role role = resolveRole(request.getRoleId());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash(PasswordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());

        Long userId = userRepository.save(user);
        user.setId(userId);

        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Using Optional API for chaining

        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    private Role resolveRole(Long roleId) {
        if (roleId != null) {
            return roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Invalid role"));
        }

        // setting SALES as default role
        return roleRepository.findByName("SALES")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
    }

    private LoginResponse mapToLoginResponse(User user) {
        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .roleName(user.getRole().getRoleName())
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roleName(user.getRole().getRoleName())
                .build();
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        // 🚫 Block deletion if user has assigned deals
        if (dealRepository.existsByAssignedUserId(userId)) {
            throw new RuntimeException(
                    "Cannot delete user with assigned deals. Reassign deals first."
            );
        }

        userRepository.deleteById(userId);
    }

}
