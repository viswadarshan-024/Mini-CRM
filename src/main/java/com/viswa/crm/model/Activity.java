package com.viswa.crm.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Activity {

    private Long id;

    private Deal deal;

    private ActivityType activityType;   // TASK or CALL or MEETING or EMAIL
    private ActivityStatus status;       // PENDING or DONE

    private String description;
    private LocalDate dueDate;

    private LocalDateTime createdAt;
}
