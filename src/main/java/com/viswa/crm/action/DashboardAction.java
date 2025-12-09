package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.activity.ActivityResponse;
import com.viswa.crm.dto.dashboard.DashboardSummaryResponse;
import com.viswa.crm.security.strategy.RoleStrategy;
import com.viswa.crm.service.ActivityService;
import com.viswa.crm.service.DashboardService;
import lombok.Getter;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.opensymphony.xwork2.Action.*;


@AllowedMethods({"execute", "add", "edit", "save", "delete"})

@Component
@Action(
        value = "dashboard",
        results = {
                @Result(
                        name = SUCCESS,
                        location = "dashboard.jsp",
                        type = "dispatcher"
                ),
                @Result(
                        name = LOGIN,
                        location = "login.action",
                        type = "redirect"
                )
                // Need to add this when adding global exception handler
//                @Result(
//                        name = ERROR,
//                        location = "error.action",
//                        type = "redirect"
//                )
        }
)

public class DashboardAction extends BaseAction {

    @Autowired
    private DashboardService dashboardService;

    @Getter
    private DashboardSummaryResponse summary;

    @Getter
    private List<ActivityResponse> recentItems;

    @Autowired
    private ActivityService activityService;

    private RoleStrategy roleStrategy;

    @Getter
    private Object user;

    @Override
    public String execute() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        this.user = getCurrentUser();

        summary = dashboardService.getSummary(getCurrentUser().getUserId());

        recentItems = activityService.getRecentActivities(10);

        return SUCCESS;
    }
}
