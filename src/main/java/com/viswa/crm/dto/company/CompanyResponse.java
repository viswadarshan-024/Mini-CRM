package com.viswa.crm.dto.company;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
}
