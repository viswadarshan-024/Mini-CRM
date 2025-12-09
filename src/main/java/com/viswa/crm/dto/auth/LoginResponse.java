package com.viswa.crm.dto.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {

    private Long userId;
    private String username;
    private String fullName;
    private String roleName;

    // role detection
    public boolean isAdmin() {
        return "ADMIN".equals(roleName);
    }

    public boolean isManager() {
        return "MANAGER".equals(roleName);
    }

    public boolean isSales() {
        return "SALES".equals(roleName);
    }
}