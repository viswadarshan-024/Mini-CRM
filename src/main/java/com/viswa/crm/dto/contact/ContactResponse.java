package com.viswa.crm.dto.contact;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContactResponse {

    private Long id;

    private Long companyId;
    private String companyName;

    private String name;
    private String email;
    private String phone;
    private String jobTitle;

    private LocalDateTime createdAt;
}
