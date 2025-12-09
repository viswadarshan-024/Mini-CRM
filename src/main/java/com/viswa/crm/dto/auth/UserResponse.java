package com.viswa.crm.dto.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String roleName;
}
