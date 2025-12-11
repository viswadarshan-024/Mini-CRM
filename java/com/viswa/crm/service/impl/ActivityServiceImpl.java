package com.viswa.crm.service.impl;

import com.viswa.crm.dto.activity.ActivityResponse;
import com.viswa.crm.dto.activity.CreateActivityRequest;
import com.viswa.crm.dto.activity.UpdateActivityRequest;
import com.viswa.crm.model.Activity;
import com.viswa.crm.model.ActivityStatus;
import com.viswa.crm.model.ActivityType;
import com.viswa.crm.model.Deal;
import com.viswa.crm.repository.ActivityRepository;
import com.viswa.crm.repository.DealRepository;
import com.viswa.crm.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final DealRepository dealRepository;

    @Override
    @Transactional
    public ActivityResponse createActivity(CreateActivityRequest request) {

        Deal deal = dealRepository.findById(request.getDealId())
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        validateDueDate(request.getDueDate());

        Activity activity = new Activity();
        activity.setDeal(deal);
        activity.setActivityType(request.getType());
        activity.setStatus(ActivityStatus.PENDING);
        activity.setDescription(request.getDescription());
        activity.setDueDate(request.getDueDate());
        activity.setCreatedAt(LocalDateTime.now());

        Long id = activityRepository.save(activity);
        activity.setId(id);

        return mapToResponse(activity);
    }

    @Override
    @Transactional
    public ActivityResponse updateActivity(Long activityId, UpdateActivityRequest request) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        if (activity.getStatus() == ActivityStatus.DONE) {
            throw new RuntimeException("Completed activity cannot be modified");
        }

        if (request.getType() != null) {
            activity.setActivityType(request.getType());
        }

        if (request.getDescription() != null) {
            activity.setDescription(request.getDescription());
        }

        if (request.getDueDate() != null) {
            validateDueDate(request.getDueDate());
            activity.setDueDate(request.getDueDate());
        }

        if (request.getStatus() != null) {
            validateStatusTransition(activity.getStatus(), request.getStatus());
            activity.setStatus(request.getStatus());
        }

        activityRepository.update(activity);
        return mapToResponse(activity);
    }

    @Override
    @Transactional
    public void deleteActivity(Long activityId) {

        if (!activityRepository.existsById(activityId)) {
            throw new RuntimeException("Activity not found");
        }

        activityRepository.deleteById(activityId);
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityResponse getActivityById(Long activityId) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        return mapToResponse(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponse> getActivitiesByDeal(Long dealId) {

        return activityRepository.findByDealId(dealId)
                .stream()
                .map(this::mapToResponse)
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponse> getPendingActivities() {

        return activityRepository.findByStatus(ActivityStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .collect(toList());
    }

    @Override
    @Transactional
    public ActivityResponse markAsDone(Long activityId) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        if (activity.getStatus() == ActivityStatus.DONE) {
            throw new RuntimeException("Activity already completed");
        }

        activity.setStatus(ActivityStatus.DONE);
        activityRepository.update(activity);

        return mapToResponse(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponse> getRecentActivities(int limit) {

        List<Activity> activities =
                activityRepository.findRecentActivities(limit);

        // 1. Collect deal ids
        List<Long> dealIds = activities.stream()
                .map(a -> a.getDeal().getId())
                .distinct().collect(toList());

        // 2. Load deals in bulk
        Map<Long, String> dealTitleMap =
                dealRepository.findByIds(dealIds)
                        .stream()
                        .collect(Collectors.toMap(
                                Deal::getId,
                                Deal::getTitle
                        ));

        // 3. Map responses
        return activities.stream()
                .map(a -> ActivityResponse.builder()
                        .id(a.getId())
                        .dealId(a.getDeal().getId())
                        .dealTitle(dealTitleMap.get(a.getDeal().getId()))
                        .type(a.getActivityType())
                        .status(a.getStatus())
                        .description(a.getDescription())
                        .dueDate(a.getDueDate())
                        .createdAt(a.getCreatedAt())
                        .build()
                )
                .collect(toList());
    }

    private void validateDueDate(LocalDate dueDate) {
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Due date cannot be in the past");
        }
    }

    private void validateStatusTransition(ActivityStatus current, ActivityStatus next) {
        if (current == ActivityStatus.DONE && next != ActivityStatus.DONE) {
            throw new RuntimeException("Completed activity cannot change state");
        }
    }

    private ActivityResponse mapToResponse(Activity activity) {
        return ActivityResponse.builder()
                .id(activity.getId())
                .dealId(activity.getDeal().getId())
                .dealTitle(activity.getDeal().getTitle())
                .type(activity.getActivityType())
                .status(activity.getStatus())
                .description(activity.getDescription())
                .dueDate(activity.getDueDate())
                .createdAt(activity.getCreatedAt())
                .build();
    }
}
