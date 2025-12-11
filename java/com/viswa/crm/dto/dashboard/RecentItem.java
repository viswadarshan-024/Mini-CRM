package com.viswa.crm.dto.dashboard;

import lombok.Data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RecentItem {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
}
