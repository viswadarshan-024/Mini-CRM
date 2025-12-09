package com.viswa.crm.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contact {

    private Long id;

    private Company company;

    private String name;
    private String email;
    private String phone;
    private String jobTitle;

    private LocalDateTime createdAt;
}
