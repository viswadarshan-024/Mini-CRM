package com.viswa.crm.service;

import com.viswa.crm.dto.activity.ActivityResponse;
import com.viswa.crm.dto.activity.CreateActivityRequest;
import com.viswa.crm.dto.activity.UpdateActivityRequest;

import java.util.List;

public interface ActivityService {

    ActivityResponse createActivity(CreateActivityRequest request);

    ActivityResponse updateActivity(Long activityId, UpdateActivityRequest request);

    void deleteActivity(Long activityId);

    ActivityResponse getActivityById(Long activityId);

    List<ActivityResponse> getActivitiesByDeal(Long dealId);

    List<ActivityResponse> getPendingActivities();

    ActivityResponse markAsDone(Long activityId);

    List<ActivityResponse> getRecentActivities(int limit);
}
