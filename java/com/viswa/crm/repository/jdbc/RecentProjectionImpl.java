package com.viswa.crm.repository.jdbc;

import com.viswa.crm.repository.RecentProjection;

import java.time.LocalDateTime;

public class RecentProjectionImpl implements RecentProjection {

    private final Long id;
    private final String title;
    private final LocalDateTime createdAt;

    public RecentProjectionImpl(Long id, String title, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
