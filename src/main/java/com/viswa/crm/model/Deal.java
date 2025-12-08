package com.viswa.crm.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Deal {

    private Long id;

    private String title;
    private BigDecimal amount;
    private DealStatus status;

    private User assignedUser;
    private Contact contact;
    private Company company;

    private LocalDateTime createdAt;
}
