package com.viswa.crm.repository;

import com.viswa.crm.model.Activity;
import com.viswa.crm.model.ActivityStatus;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository {

    Optional<Activity> findById(Long id);

    Long save(Activity activity);

    void update(Activity activity);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Activity> findByDealId(Long dealId);

    List<Activity> findByStatus(ActivityStatus status);

    List<Activity> findRecentActivities(int limit);
}
