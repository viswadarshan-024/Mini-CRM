package com.viswa.crm.dto.auth;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String roleName;
}
