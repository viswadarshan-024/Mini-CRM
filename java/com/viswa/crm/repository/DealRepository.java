package com.viswa.crm.repository;

import com.viswa.crm.model.Deal;
import com.viswa.crm.model.DealStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DealRepository {

    Optional<Deal> findById(Long id);

    Long save(Deal deal);

    void update(Deal deal);

    void deleteById(Long id);

    List<Deal> findByUserId(Long userId);

    List<Deal> findByCompanyId(Long companyId);

    List<Deal> search(String keyword);

    Map<DealStatus, Long> countByStatus();

    boolean existsById(Long id);

    List<Deal> findByStatus(DealStatus status);

    List<Deal> findByIds(List<Long> ids);

    boolean existsActiveDealByContactId(Long contactId);

    boolean existsByCompanyId(Long companyId);

    boolean existsByAssignedUserId(Long userId);
}
