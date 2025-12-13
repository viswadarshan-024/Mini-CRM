package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.activity.ActivityResponse;
import com.viswa.crm.dto.activity.CreateActivityRequest;
import com.viswa.crm.dto.activity.UpdateActivityRequest;
import com.viswa.crm.dto.common.ApiResponse;
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

@Getter
@Setter
@Component
@ParentPackage("json-default")
@Namespace("/")
public class ActivityAction extends BaseAction {

    @Autowired
    private transient ActivityService activityService;

    private ApiResponse<Object> apiResponse = new ApiResponse<>();

    private Long dealId;
    private ActivityType type;
    private String description;
    private LocalDate dueDate;
    private Long id;
    private CreateActivityRequest createRequest;
    private UpdateActivityRequest updateRequest;

    // ===== LIST =====
    @Action(
            value = "activity",
            results = {
                    @Result(
                            name = SUCCESS,
                            type = "json",
                            params = {
                                    "root", "apiResponse",
                                    "excludeNullProperties", "true"
                            }
                    ),
                    @Result(
                            name = LOGIN,
                            type = "json",
                            params = {
                                    "root", "apiResponse",
                                    "excludeNullProperties", "true"
                            }
                    ),
                    @Result(
                            name = ERROR,
                            type = "json",
                            params = {
                                    "root", "apiResponse",
                                    "excludeNullProperties", "true"
                            }
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

        List<ActivityResponse> activities = activityService.getRecentActivities(100);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Activities fetched successfully");
        apiResponse.setData(activities);

        return SUCCESS;
    }

    // ===== CREATE =====
    @Action(
            value = "activity-save",
            results = {
                    @Result(name = SUCCESS, type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = LOGIN,   type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = ERROR,   type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"})
            }
    )
    public String save() {

        if (!isAuthenticated()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authenticated");
            return LOGIN;
        }
        if (!role().canCreateActivity()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authorized to create activity");
            return ERROR;
        }

        CreateActivityRequest request = new CreateActivityRequest();
        request.setDealId(dealId);
        request.setType(type);
        request.setDescription(description);
        request.setDueDate(dueDate);

        activityService.createActivity(request);

        apiResponse.setSuccess(true);
        apiResponse.setMessage("Activity created successfully");
        apiResponse.setData(null);

        return SUCCESS;
    }

    // ===== GET ONE =====
    @Action(
            value = "activity-edit",
            results = {
                    @Result(name = SUCCESS, type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = LOGIN,   type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = ERROR,   type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"})
            }
    )
    public String edit() {

        if (!isAuthenticated()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authenticated");
            return LOGIN;
        }

        ActivityResponse activity = activityService.getActivityById(id);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Activity fetched successfully");
        apiResponse.setData(activity);

        return SUCCESS;
    }

    // ===== UPDATE =====
    @Action(
            value = "activity-update",
            results = {
                    @Result(name = SUCCESS, type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = LOGIN,   type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = ERROR,   type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"})
            }
    )
    public String update() {

        if (!isAuthenticated()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authenticated");
            return LOGIN;
        }

        activityService.updateActivity(id, updateRequest);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Activity updated successfully");
        apiResponse.setData(null);

        return SUCCESS;
    }

    // ===== MARK DONE =====
    @Action(
            value = "activity-done",
            results = {
                    @Result(name = SUCCESS, type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = LOGIN,   type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"})
            }
    )
    public String markDone() {

        if (!isAuthenticated()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authenticated");
            return LOGIN;
        }

        activityService.markAsDone(id);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Activity marked as done");
        apiResponse.setData(null);

        return SUCCESS;
    }

    // ===== DELETE =====
    @Action(
            value = "activity-delete",
            results = {
                    @Result(name = SUCCESS, type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = LOGIN,   type = "json", params = {"root", "apiResponse", "excludeNullProperties", "true"})
            }
    )
    public String delete() {

        if (!isAuthenticated()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authenticated");
            return LOGIN;
        }

        activityService.deleteActivity(id);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Activity deleted successfully");
        apiResponse.setData(null);

        return SUCCESS;
    }
}
