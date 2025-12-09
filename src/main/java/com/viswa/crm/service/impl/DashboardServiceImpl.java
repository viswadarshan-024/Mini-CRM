package com.viswa.crm.service.impl;

import com.viswa.crm.dto.dashboard.DashboardSummaryResponse;
import com.viswa.crm.dto.dashboard.RecentItem;
import com.viswa.crm.model.DealStatus;
import com.viswa.crm.repository.DashboardRepository;
import com.viswa.crm.repository.RecentProjection;
import com.viswa.crm.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary(Long userId) {

        DashboardSummaryResponse response = new DashboardSummaryResponse();

        response.setTotalCompanies(dashboardRepository.countCompanies());
        response.setTotalContacts(dashboardRepository.countContacts());
        response.setTotalDeals(dashboardRepository.countDeals());
        response.setPendingActivities(dashboardRepository.countPendingActivities());

        Map<DealStatus, Long> dealStats = dashboardRepository.countDealsByStatus();

        response.setDealsNew(dealStats.getOrDefault(DealStatus.NEW, 0L));
        response.setDealsInProgress(dealStats.getOrDefault(DealStatus.IN_PROGRESS, 0L));
        response.setDealsClosed(dealStats.getOrDefault(DealStatus.CLOSED, 0L));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecentItem> getRecentDeals(int limit) {

        return dashboardRepository.findRecentDeals(limit)
                .stream()
                .map(this::mapToRecentItem)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecentItem> getRecentContacts(int limit) {

        return dashboardRepository.findRecentContacts(limit)
                .stream()
                .map(this::mapToRecentItem)
                .collect(Collectors.toList());
    }

    private RecentItem mapToRecentItem(RecentProjection projection) {

        RecentItem item = new RecentItem();
        item.setId(projection.getId());
        item.setTitle(projection.getTitle());
        item.setCreatedAt(projection.getCreatedAt());
        return item;
    }
}
