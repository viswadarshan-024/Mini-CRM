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
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static com.opensymphony.xwork2.Action.*;

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

    // --------------------
    // Form fields
    // --------------------
    private Long id;
    private String title;
    private BigDecimal amount;
    private Long assignedUserId;
    private Long companyId;
    private Long contactId;
    private DealStatus status;

    private String keyword;

    // --------------------
    // View data
    // --------------------
    private List<DealResponse> deals;
    private List<UserResponse> users;
    private List<CompanyResponse> companies;
    private List<ContactResponse> contacts;
    private DealStatus[] statuses = DealStatus.values();

    @Override
    public String execute() {

        if (!isAuthenticated()) return LOGIN;

        if (isSales()) {
            deals = dealService.getDealsByUser(getCurrentUser().getUserId());
        } else if (keyword != null && !keyword.isBlank()) {
            deals = dealService.searchDeals(keyword);
        } else {
            deals = dealService.searchDeals("");
        }

        return SUCCESS;
    }

    public String add() {

        if (!isAuthenticated()) return LOGIN;

//        if (!isAdmin() || !isManager()) {
//            return ERROR;
//        }

        loadDropdowns();

        return INPUT;
    }

    public String edit() {

        if (!isAuthenticated()) return LOGIN;

        DealResponse deal = dealService.getDealById(id);

        if (isSales() && !deal.getAssignedUserId().equals(getCurrentUser().getUserId())) {
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
                CreateDealRequest request = new CreateDealRequest();
                request.setTitle(title);
                request.setAmount(amount);
                request.setCompanyId(companyId);
                request.setContactId(contactId);

                if (isSales()) {
                    request.setAssignedUserId(getCurrentUser().getUserId());
                } else {
                    request.setAssignedUserId(assignedUserId);
                }

                dealService.createDeal(request);

            } else {
                UpdateDealRequest request = new UpdateDealRequest();
                request.setTitle(title);
                request.setAmount(amount);
                request.setAssignedUserId(
                        isSales() ? null : assignedUserId
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

    // --------------------
    // DELETE
    // --------------------
    public String delete() {

        if (!isAuthenticated()) return LOGIN;
        if (!isAdmin()) return ERROR;

        dealService.deleteDeal(id);
        return SUCCESS;
    }

    // --------------------
    // CHANGE STATUS
    // --------------------
    public String changeStatus() {

        if (!isAuthenticated()) return LOGIN;

        ChangeDealStatusRequest request = new ChangeDealStatusRequest();
        request.setStatus(status);

        dealService.changeDealStatus(id, request);
        return SUCCESS;
    }

    // --------------------
    // HELPERS
    // --------------------
    private void loadDropdowns() {
        companies = companyService.getAllCompanies();
        users = authService.getAllUsers();
        if (companyId != null) {
            contacts = contactService.getContactsByCompany(companyId);
        }
    }
}
