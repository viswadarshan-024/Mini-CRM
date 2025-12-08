package com.viswa.crm.repository;

import java.time.LocalDateTime;

public interface RecentProjection {

    Long getId();

    String getTitle();

    LocalDateTime getCreatedAt();
}
