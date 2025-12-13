package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.activity.ActivityResponse;
import com.viswa.crm.dto.dashboard.DashboardSummaryResponse;
import com.viswa.crm.dto.common.ApiResponse;
import com.viswa.crm.security.strategy.RoleStrategy;
import com.viswa.crm.service.ActivityService;
import com.viswa.crm.service.DashboardService;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.opensymphony.xwork2.Action.*;

@Component
@Getter
@Setter
@ParentPackage("json-default")
@Namespace("/")
@AllowedMethods({"execute"})
public class DashboardAction extends BaseAction {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ActivityService activityService;

    private ApiResponse<Object> apiResponse = new ApiResponse<>();

    private DashboardSummaryResponse summary;
    private List<ActivityResponse> recentItems;
    private Object user;
    private RoleStrategy role;

    @Action(
            value = "dashboard",
            results = {
                    @Result(
                            name = SUCCESS,
                            type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}
                    ),
                    @Result(
                            name = LOGIN,
                            type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}
                    )
            }
    )
    @Override
    public String execute() {

        if (!isAuthenticated()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authenticated");
            apiResponse.setData(null);
            return LOGIN;
        }

        this.user = getCurrentUser();
        this.role = role();

        this.summary = dashboardService.getSummary(getCurrentUser().getUserId());
        this.recentItems = activityService.getRecentActivities(10);

        // Wrap all in one JSON object
        DashboardPayload payload = new DashboardPayload(
                user,
                role,
                summary,
                recentItems
        );

        apiResponse.setSuccess(true);
        apiResponse.setMessage("Dashboard data loaded successfully");
        apiResponse.setData(payload);

        return SUCCESS;
    }

    // inner static class for clean JSON transport
    // CORRECT: 'public' allows Struts to read the fields and convert to JSON
    @Getter
    public static class DashboardPayload {
        private final Object user;
        private final RoleStrategy role;
        private final DashboardSummaryResponse summary;
        private final List<ActivityResponse> recentItems;

        public DashboardPayload(
                Object user,
                RoleStrategy role,
                DashboardSummaryResponse summary,
                List<ActivityResponse> recentItems
        ) {
            this.user = user;
            this.role = role;
            this.summary = summary;
            this.recentItems = recentItems;
        }
    }
}
