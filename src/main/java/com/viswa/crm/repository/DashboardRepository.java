package com.viswa.crm.repository;

import com.viswa.crm.model.DealStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface DashboardRepository {

    long countCompanies();

    long countContacts();

    long countDeals();

    long countPendingActivities();

    Map<DealStatus, Long> countDealsByStatus();

    List<RecentProjection> findRecentDeals(int limit);

    List<RecentProjection> findRecentContacts(int limit);
}
