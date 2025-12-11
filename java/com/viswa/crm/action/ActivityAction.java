package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.activity.ActivityResponse;
import com.viswa.crm.dto.activity.CreateActivityRequest;
import com.viswa.crm.dto.activity.UpdateActivityRequest;
import com.viswa.crm.model.ActivityType;
import com.viswa.crm.service.ActivityService;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.opensymphony.xwork2.Action.SUCCESS;

@Getter
@Setter
@Component
//@ParentPackage("json-default")
//@Namespace("/")
public class ActivityAction extends BaseAction {

    // Dependency Injection with Spring
    @Autowired
    private ActivityService activityService;

    private List<ActivityResponse> activities;

    private ActivityResponse activity;

    private Long dealId;
    private ActivityType type;
    private String description;
    private LocalDate dueDate;

    private Long id;

    private CreateActivityRequest createRequest;

    private UpdateActivityRequest updateRequest;

    @Action(
            value = "activity",
            results = {
                    @Result(name = SUCCESS, location = "activity/activity-list.jsp"),
                    @Result(name = LOGIN, location = "login.action", type = "redirect")
            }
    )
    @Override
    public String execute() {

        if (!isAuthenticated()) return LOGIN;

        activities = activityService.getRecentActivities(100);
        return SUCCESS;
    }

    @Action(
            value = "activity-add",
            results = {
                    @Result(name = SUCCESS, location = "activity/activity-form.jsp"),
                    @Result(name = LOGIN, location = "login.action", type = "redirect")
            }
    )
    public String add() {

        if (!isAuthenticated()) return LOGIN;
        if (!role().canCreateActivity()) return ERROR;

        return SUCCESS;
    }

    @Action(
            value = "activity-save",
            results = {
                    @Result(name = SUCCESS, type = "redirect", location = "activity.action"),
                    @Result(name = INPUT, location = "activity/activity-form.jsp"),
                    @Result(name = LOGIN, location = "login.action", type = "redirect")
            }
    )

    public String save() {

        if (!isAuthenticated()) return LOGIN;
        if (!role().canCreateActivity()) return ERROR;

        CreateActivityRequest request = new CreateActivityRequest();
        request.setDealId(dealId);
        request.setType(type);
        request.setDescription(description);
        request.setDueDate(dueDate);

        activityService.createActivity(request);
        return SUCCESS;
    }


    @Action(
            value = "activity-edit",
            results = {
                    @Result(name = SUCCESS, location = "activity/activity-form.jsp"),
                    @Result(name = LOGIN, location = "login.action", type = "redirect")
            }
    )
    public String edit() {

        if (!isAuthenticated()) return LOGIN;

        activity = activityService.getActivityById(id);
        return SUCCESS;
    }

    @Action(
            value = "activity-update",
            results = {
                    @Result(name = SUCCESS, type = "redirect", location = "activity.action"),
                    @Result(name = INPUT, location = "activity/activity-form.jsp"),
                    @Result(name = LOGIN, location = "login.action", type = "redirect")
            }
    )
    public String update() {

        if (!isAuthenticated()) return LOGIN;

        activityService.updateActivity(id, updateRequest);
        return SUCCESS;
    }

    @Action(
            value = "activity-done",
            results = {
                    @Result(name = SUCCESS, type = "redirect", location = "activity.action"),
                    @Result(name = LOGIN, location = "login.action", type = "redirect")
            }
    )
    public String markDone() {

        if (!isAuthenticated()) return LOGIN;

        activityService.markAsDone(id);
        return SUCCESS;
    }

    @Action(
            value = "activity-delete",
            results = {
                    @Result(name = SUCCESS, type = "redirect", location = "activity.action"),
                    @Result(name = LOGIN, location = "login.action", type = "redirect")
            }
    )
    public String delete() {

        if (!isAuthenticated()) return LOGIN;

        activityService.deleteActivity(id);
        return SUCCESS;
    }
}