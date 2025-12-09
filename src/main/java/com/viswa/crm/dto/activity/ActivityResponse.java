package com.viswa.crm.dto.activity;

import com.viswa.crm.model.ActivityStatus;
import com.viswa.crm.model.ActivityType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


// Builder Pattern
@Builder
@Data
public class ActivityResponse {

    private Long id;

    private Long dealId;
    private String dealTitle;

    private ActivityType type;
    private ActivityStatus status;

    private String description;
    private LocalDate dueDate;

    private LocalDateTime createdAt;
}
