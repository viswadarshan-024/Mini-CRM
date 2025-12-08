package com.viswa.crm.service;

import com.viswa.crm.dto.dashboard.DashboardSummaryResponse;
import com.viswa.crm.dto.dashboard.RecentItem;

import java.util.List;

public interface DashboardService {

    DashboardSummaryResponse getSummary(Long userId);

    List<RecentItem> getRecentDeals(int limit);

    List<RecentItem> getRecentContacts(int limit);
}
