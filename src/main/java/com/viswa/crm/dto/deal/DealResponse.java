package com.viswa.crm.dto.deal;

import com.viswa.crm.model.DealStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DealResponse {

    private Long id;
    private String title;
    private BigDecimal amount;
    private DealStatus status;

    private Long assignedUserId;
    private String assignedUserName;

    private Long companyId;
    private String companyName;

    private Long contactId;
    private String contactName;

    private LocalDateTime createdAt;
}
