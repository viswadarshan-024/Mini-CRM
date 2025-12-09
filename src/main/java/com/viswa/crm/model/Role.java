package com.viswa.crm.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {

    private Long id;
    private String roleName; // ADMIN, MANAGER, SALES
    private LocalDateTime createdAt;
}
