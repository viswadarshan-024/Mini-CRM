package com.viswa.crm.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Company {

    private Long id;

    private String name;
    private String email;
    private String phone;
    private String address;

    private LocalDateTime createdAt;
}
