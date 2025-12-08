package com.viswa.crm.dto.dashboard;

import lombok.Data;

@Data
public class DashboardSummaryResponse {

    private long totalCompanies;
    private long totalContacts;
    private long totalDeals;

    private long dealsNew;
    private long dealsInProgress;
    private long dealsClosed;

    private long pendingActivities;
}
