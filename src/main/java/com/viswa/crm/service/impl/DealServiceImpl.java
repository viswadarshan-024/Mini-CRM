package com.viswa.crm.service.impl;

import com.viswa.crm.dto.deal.ChangeDealStatusRequest;
import com.viswa.crm.dto.deal.CreateDealRequest;
import com.viswa.crm.dto.deal.DealResponse;
import com.viswa.crm.dto.deal.UpdateDealRequest;
import com.viswa.crm.model.Company;
import com.viswa.crm.model.Contact;
import com.viswa.crm.model.Deal;
import com.viswa.crm.model.DealStatus;
import com.viswa.crm.model.User;
import com.viswa.crm.repository.CompanyRepository;
import com.viswa.crm.repository.ContactRepository;
import com.viswa.crm.repository.DealRepository;
import com.viswa.crm.repository.UserRepository;
import com.viswa.crm.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;

    @Override
    @Transactional
    public DealResponse createDeal(CreateDealRequest request) {

        User user = userRepository.findById(request.getAssignedUserId())
                .orElseThrow(() -> new RuntimeException("Assigned user not found"));

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Contact contact = null;
        if (request.getContactId() != null) {
            contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new RuntimeException("Contact not found"));

            if (!contact.getCompany().getId().equals(company.getId())) {
                throw new RuntimeException("Contact does not belong to the given company");
            }
        }

        Deal deal = new Deal();
        deal.setTitle(request.getTitle());
        deal.setAmount(request.getAmount());
        deal.setStatus(DealStatus.NEW);
        deal.setAssignedUser(user);
        deal.setCompany(company);
        deal.setContact(contact);
        deal.setCreatedAt(LocalDateTime.now());

        Long id = dealRepository.save(deal);
        deal.setId(id);

        return mapToResponse(deal);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<DealStatus, Long> getDealCountByStatus() {

        Map<DealStatus, Long> raw = dealRepository.countByStatus();
        Map<DealStatus, Long> result = new EnumMap<>(DealStatus.class);

        for (DealStatus status : DealStatus.values()) {
            result.put(status, raw.getOrDefault(status, 0L));
        }

        return result;
    }

    @Override
    @Transactional
    public DealResponse updateDeal(Long dealId, UpdateDealRequest request) {

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        if (request.getTitle() != null) {
            deal.setTitle(request.getTitle());
        }

        if (request.getAmount() != null) {
            deal.setAmount(request.getAmount());
        }

        if (request.getAssignedUserId() != null) {
            User user = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            deal.setAssignedUser(user);
        }

        if (request.getContactId() != null) {
            Contact contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new RuntimeException("Contact not found"));

            if (!contact.getCompany().getId().equals(deal.getCompany().getId())) {
                throw new RuntimeException("Contact does not belong to deal company");
            }

            deal.setContact(contact);
        }

        dealRepository.update(deal);
        return mapToResponse(deal);
    }

    @Override
    @Transactional
    public void deleteDeal(Long dealId) {

        if (!dealRepository.existsById(dealId)) {
            throw new RuntimeException("Deal not found");
        }

        dealRepository.deleteById(dealId);
    }

    @Override
    @Transactional(readOnly = true)
    public DealResponse getDealById(Long dealId) {

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        return mapToResponse(deal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DealResponse> getDealsByUser(Long userId) {

        return dealRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DealResponse> getDealsByCompany(Long companyId) {

        return dealRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DealResponse> searchDeals(String keyword) {

        return dealRepository.search(keyword)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DealResponse changeDealStatus(Long dealId, ChangeDealStatusRequest request) {

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        validateStatusTransition(deal.getStatus(), request.getStatus());
        deal.setStatus(request.getStatus());

        dealRepository.update(deal);
        return mapToResponse(deal);
    }

    private void validateStatusTransition(DealStatus current, DealStatus next) {

        if (current == DealStatus.CLOSED) {
            throw new RuntimeException("Closed deals cannot be modified");
        }

        switch (current) {
            case NEW:
                if (next != DealStatus.QUALIFIED) {
                    throw new RuntimeException("Deal must be QUALIFIED first");
                }
                break;

            case QUALIFIED:
                if (next != DealStatus.IN_PROGRESS) {
                    throw new RuntimeException("Deal must move to IN_PROGRESS");
                }
                break;

            case IN_PROGRESS:
                if (next != DealStatus.DELIVERED) {
                    throw new RuntimeException("Deal must be DELIVERED");
                }
                break;

            case DELIVERED:
                if (next != DealStatus.CLOSED) {
                    throw new RuntimeException("Delivered deal can only be CLOSED");
                }
                break;

            default:
                throw new RuntimeException("Invalid deal status transition");
        }
    }

    private DealResponse mapToResponse(Deal deal) {

        return DealResponse.builder()
                .id(deal.getId())
                .title(deal.getTitle())
                .amount(deal.getAmount())
                .status(deal.getStatus())
                .createdAt(deal.getCreatedAt())
                .assignedUserId(deal.getAssignedUser().getId())
                .assignedUserName(deal.getAssignedUser().getFullName())
                .companyId(deal.getCompany().getId())
                .companyName(deal.getCompany().getName())
                .contactId(
                        deal.getContact() != null ? deal.getContact().getId() : null
                )
                .contactName(
                        deal.getContact() != null ? deal.getContact().getName() : null
                )
                .build();
    }

}
