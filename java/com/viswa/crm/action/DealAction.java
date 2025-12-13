package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.auth.UserResponse;
import com.viswa.crm.dto.common.ApiResponse;
import com.viswa.crm.dto.company.CompanyResponse;
import com.viswa.crm.dto.contact.ContactResponse;
import com.viswa.crm.dto.deal.ChangeDealStatusRequest;
import com.viswa.crm.dto.deal.CreateDealRequest;
import com.viswa.crm.dto.deal.DealResponse;
import com.viswa.crm.dto.deal.UpdateDealRequest;
import com.viswa.crm.model.DealStatus;
import com.viswa.crm.service.AuthService;
import com.viswa.crm.service.CompanyService;
import com.viswa.crm.service.ContactService;
import com.viswa.crm.service.DealService;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.SUCCESS;

@AllowedMethods({"execute", "add", "edit", "save", "delete", "changeStatus"})
@Component
@Getter
@Setter
@ParentPackage("json-default")
@Namespace("/")
public class DealAction extends BaseAction {

    @Autowired
    private transient DealService dealService;

    @Autowired
    private transient CompanyService companyService;

    @Autowired
    private transient ContactService contactService;

    @Autowired
    private transient AuthService authService;

    // JSON root
    private ApiResponse<Object> apiResponse = new ApiResponse<>();

    // Request fields
    private Long id;
    private String title;
    private BigDecimal amount;
    private Long assignedUserId;
    private Long companyId;
    private Long contactId;
    private DealStatus status;
    private String keyword;

    // For dropdowns / extra data
    private List<UserResponse> users;
    private List<CompanyResponse> companies;
    private List<ContactResponse> contacts;
    private DealStatus[] statuses = DealStatus.values();

    // ===== LIST DEALS =====
    @Action(
            value = "deal",
            results = {
                    @Result(name = SUCCESS, type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = LOGIN,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = ERROR,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"})
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

        List<DealResponse> deals;
        if (role().canViewAllDeals()) {
            if (keyword != null && !keyword.isBlank()) {
                deals = dealService.searchDeals(keyword);
                apiResponse.setMessage("Deals searched successfully");
            } else {
                deals = dealService.searchDeals("");
                apiResponse.setMessage("Deals fetched successfully");
            }
        } else {
            deals = dealService.getDealsByUser(getCurrentUser().getUserId());
            apiResponse.setMessage("Deals for current user fetched successfully");
        }

        apiResponse.setSuccess(true);
        apiResponse.setData(deals);
        return SUCCESS;
    }
    @Action(
            value = "deal-add",
            results = {
                    @Result(name = SUCCESS, type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = LOGIN,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = ERROR,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"})
            }
    )
    public String add() {

        if (!isAuthenticated()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authenticated");
            return LOGIN;
        }

        loadDropdowns();

        // Build a simple map as payload
        Map<String, Object> formData = new HashMap<>();
        formData.put("statuses", statuses);
        formData.put("companies", companies);
        formData.put("users", users);
        formData.put("contacts", contacts);

        apiResponse.setSuccess(true);
        apiResponse.setMessage("Ready to create deal");
        apiResponse.setData(formData);

        return SUCCESS;
    }

    // ===== GET ONE DEAL =====
    @Action(
            value = "deal-edit",
            results = {
                    @Result(name = SUCCESS, type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = LOGIN,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = ERROR,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"})
            }
    )
    public String edit() {

        if (!isAuthenticated()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authenticated");
            return LOGIN;
        }

        DealResponse deal = dealService.getDealById(id);

        if (!role().canViewAllDeals()
                && !deal.getAssignedUserId().equals(getCurrentUser().getUserId())) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authorized to view this deal");
            return ERROR;
        }

        loadDropdowns();

        apiResponse.setSuccess(true);
        apiResponse.setMessage("Deal fetched successfully");
        apiResponse.setData(deal);
        return SUCCESS;
    }

    // ===== CREATE / UPDATE =====
    @Action(
            value = "deal-save",
            results = {
                    @Result(name = SUCCESS, type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = INPUT,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = LOGIN,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = ERROR,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"})
            }
    )
    public String save() {

        if (!isAuthenticated()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authenticated");
            return LOGIN;
        }

        try {
            if (id == null) {
                if (!role().canCreateDeal()) {
                    apiResponse.setSuccess(false);
                    apiResponse.setMessage("Not authorized to create deal");
                    return ERROR;
                }

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

                apiResponse.setSuccess(true);
                apiResponse.setMessage("Deal created successfully");
                apiResponse.setData(null);

            } else {
                UpdateDealRequest request = new UpdateDealRequest();
                request.setTitle(title);
                request.setAmount(amount);
                request.setAssignedUserId(
                        role().canEditDeal() ? null : assignedUserId
                );
                request.setContactId(contactId);

                dealService.updateDeal(id, request);

                apiResponse.setSuccess(true);
                apiResponse.setMessage("Deal updated successfully");
                apiResponse.setData(null);
            }

            return SUCCESS;

        } catch (Exception ex) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage(ex.getMessage());
            apiResponse.setData(null);
            return INPUT;
        }
    }

    // ===== DELETE =====
    @Action(
            value = "deal-delete",
            results = {
                    @Result(name = SUCCESS, type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = LOGIN,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = ERROR,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"})
            }
    )
    public String delete() {

        if (!isAuthenticated()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authenticated");
            return LOGIN;
        }

        if (!role().canDeleteDeal()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authorized to delete deal");
            return ERROR;
        }

        try {
            dealService.deleteDeal(id);
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Deal deleted successfully");
            apiResponse.setData(null);
            return SUCCESS;

        } catch (RuntimeException ex) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage(ex.getMessage());
            apiResponse.setData(null);
            return ERROR;
        }
    }

    // ===== CHANGE STATUS =====
    @Action(
            value = "deal-changeStatus",
            results = {
                    @Result(name = SUCCESS, type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"}),
                    @Result(name = LOGIN,   type = "json",
                            params = {"root", "apiResponse", "excludeNullProperties", "true"})
            }
    )
    public String changeStatus() {

        if (!isAuthenticated()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authenticated");
            return LOGIN;
        }

        ChangeDealStatusRequest request = new ChangeDealStatusRequest();
        request.setStatus(status);

        dealService.changeDealStatus(id, request);

        apiResponse.setSuccess(true);
        apiResponse.setMessage("Deal status changed successfully");
        apiResponse.setData(null);
        return SUCCESS;
    }

    // ===== Helper =====
    private void loadDropdowns() {
        companies = companyService.getAllCompanies();
        users = authService.getAllUsers();

        if (companyId != null) {
            contacts = contactService.getContactsByCompany(companyId);
        } else {
            contacts = List.of();
        }
    }
}
