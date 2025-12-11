package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.deal.*;
import com.viswa.crm.dto.company.CompanyResponse;
import com.viswa.crm.dto.contact.ContactResponse;
import com.viswa.crm.dto.auth.UserResponse;
import com.viswa.crm.model.DealStatus;
import com.viswa.crm.service.CompanyService;
import com.viswa.crm.service.ContactService;
import com.viswa.crm.service.DealService;
import com.viswa.crm.service.AuthService;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static com.opensymphony.xwork2.Action.*;


@AllowedMethods({"execute", "add", "edit", "save", "delete", "changeStatus"})

@Component
@Action(
        value = "deal",
        results = {
                @Result(name = SUCCESS, location = "deal/deal-list.jsp"),
                @Result(name = INPUT, location = "deal/deal-form.jsp"),
                @Result(name = LOGIN, location = "login.action", type = "redirect"),
                @Result(name = ERROR, location = "errors/403.jsp")
        }
)
@Getter
@Setter
public class DealAction extends BaseAction {

    @Autowired
    private DealService dealService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private AuthService authService;

    private Long id;
    private String title;
    private BigDecimal amount;
    private Long assignedUserId;
    private Long companyId;
    private Long contactId;
    private DealStatus status;

    private String keyword;

    private List<DealResponse> deals;
    private List<UserResponse> users;
    private List<CompanyResponse> companies;
    private List<ContactResponse> contacts;
    private DealStatus[] statuses = DealStatus.values();

    @Override
    public String execute() {

        if (!isAuthenticated()) return LOGIN;

        if (role().canViewAllDeals()) {
            // ADMIN, MANAGER
            if (keyword != null && !keyword.isBlank()) {
                deals = dealService.searchDeals(keyword);
            } else {
                deals = dealService.searchDeals("");
            }
        } else {
            // SALES
            deals = dealService.getDealsByUser(getCurrentUser().getUserId());
        }

        return SUCCESS;
    }


    public String add() {

        if (!isAuthenticated()) return LOGIN;

        loadDropdowns();

        return INPUT;
    }

    public String edit() {

        if (!isAuthenticated()) return LOGIN;

        DealResponse deal = dealService.getDealById(id);

        if (!role().canViewAllDeals() && !deal.getAssignedUserId().equals(getCurrentUser().getUserId())) {
            return ERROR;
        }

        this.id = deal.getId();
        this.title = deal.getTitle();
        this.amount = deal.getAmount();
        this.assignedUserId = deal.getAssignedUserId();
        this.companyId = deal.getCompanyId();
        this.contactId = deal.getContactId();
        this.status = deal.getStatus();

        loadDropdowns();

        return INPUT;
    }

    public String save() {

        if (!isAuthenticated()) return LOGIN;

        try {
            if (id == null) {
                if (!role().canCreateDeal()) return ERROR;
                CreateDealRequest request = new CreateDealRequest();
                request.setTitle(title);
                request.setAmount(amount);
                request.setCompanyId(companyId);
                request.setContactId(contactId);

                request.setAssignedUserId(
                        role().canAssignDeal()
                                ? assignedUserId
                                : getCurrentUser().getUserId()
                );

                dealService.createDeal(request);

            } else {
                UpdateDealRequest request = new UpdateDealRequest();
                request.setTitle(title);
                request.setAmount(amount);
                request.setAssignedUserId(
                        role().canEditDeal() ? null : assignedUserId
                );
                request.setContactId(contactId);

                dealService.updateDeal(id, request);
            }

            return SUCCESS;

        } catch (Exception ex) {
            addActionError(ex.getMessage());
            loadDropdowns();
            return INPUT;
        }
    }

    public String delete() {

        if (!isAuthenticated()) return LOGIN;

        // Only ADMIN and MANAGER can delete deals
        // Important edge case: SALES users should not be able to delete their own deals
        if (!role().canDeleteDeal()) return ERROR;

        try {
            dealService.deleteDeal(id);
            return SUCCESS;
        } catch (RuntimeException ex) {
            addActionError(ex.getMessage());
            return ERROR;
        }
    }

    public String changeStatus() {

        if (!isAuthenticated()) return LOGIN;

        ChangeDealStatusRequest request = new ChangeDealStatusRequest();
        request.setStatus(status);

        dealService.changeDealStatus(id, request);
        return SUCCESS;
    }

    private void loadDropdowns() {
        companies = companyService.getAllCompanies();
        users = authService.getAllUsers();

        if (companyId != null) {
            contacts = contactService.getContactsByCompany(companyId);
        } else {
            contacts = List.of(); // empty list avoids JSP error
        }
    }


}
