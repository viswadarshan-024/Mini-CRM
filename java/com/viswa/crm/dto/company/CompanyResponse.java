package com.viswa.crm.dto.company;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
public class CompanyResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
}
