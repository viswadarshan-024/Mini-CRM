package com.viswa.crm.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    private Long id;
    private Role role;

    private String username;
    private String email;
    private String passwordHash;
    private String fullName;

    private LocalDateTime createdAt;
}
