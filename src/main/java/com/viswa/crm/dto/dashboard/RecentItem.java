package com.viswa.crm.dto.dashboard;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecentItem {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
}
