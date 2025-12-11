package com.viswa.crm.dto.contact;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
