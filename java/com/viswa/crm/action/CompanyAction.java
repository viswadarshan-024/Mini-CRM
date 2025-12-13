package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.common.ApiResponse;
import com.viswa.crm.dto.company.CompanyResponse;
import com.viswa.crm.dto.company.CreateCompanyRequest;
import com.viswa.crm.dto.company.UpdateCompanyRequest;
import com.viswa.crm.service.CompanyService;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.SUCCESS;

@AllowedMethods({"execute", "add", "edit", "save", "delete"})
@Component
@Getter
@Setter
@ParentPackage("json-default")
@Namespace("/")
public class CompanyAction extends BaseAction {

    @Autowired
    private transient CompanyService companyService;   // transient: do not serialize

    // Root object for JSON
    private ApiResponse<Object> apiResponse = new ApiResponse<>();

    // Request / filter fields
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String keyword;

    // ===== LIST / SEARCH =====
    @Action(
            value = "company",
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

        List<CompanyResponse> companies;
        if (keyword != null && !keyword.isBlank()) {
            companies = companyService.searchCompanies(keyword);
            apiResponse.setMessage("Companies searched successfully");
        } else {
            companies = companyService.getAllCompanies();
            apiResponse.setMessage("Companies fetched successfully");
        }

        apiResponse.setSuccess(true);
        apiResponse.setData(companies);   // List<CompanyResponse>
        return SUCCESS;
    }

    // ===== PREPARE ADD (mostly for UI metadata) =====
    @Action(
            value = "company-add",
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

        if (!role().canCreateCompany()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authorized to create company");
            return ERROR;
        }

        apiResponse.setSuccess(true);
        apiResponse.setMessage("Ready to create company");
        apiResponse.setData(null);
        return SUCCESS;
    }

    // ===== GET ONE COMPANY FOR EDIT =====
    @Action(
            value = "company-edit",
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

        if (!role().canEditCompany()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authorized to edit company");
            return ERROR;
        }

        CompanyResponse company = companyService.getCompanyById(id);
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Company fetched successfully");
        apiResponse.setData(company);   // single CompanyResponse
        return SUCCESS;
    }

    // ===== CREATE / UPDATE =====
    @Action(
            value = "company-save",
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
                // CREATE
                if (!role().canCreateCompany()) {
                    apiResponse.setSuccess(false);
                    apiResponse.setMessage("Not authorized to create company");
                    return ERROR;
                }

                CreateCompanyRequest request = new CreateCompanyRequest();
                request.setName(name);
                request.setEmail(email);
                request.setPhone(phone);
                request.setAddress(address);

                companyService.createCompany(request);

                apiResponse.setSuccess(true);
                apiResponse.setMessage("Company created successfully");
                apiResponse.setData(null);

            } else {
                // UPDATE
                if (!role().canEditCompany()) {
                    apiResponse.setSuccess(false);
                    apiResponse.setMessage("Not authorized to update company");
                    return ERROR;
                }

                UpdateCompanyRequest request = new UpdateCompanyRequest();
                request.setName(name);
                request.setEmail(email);
                request.setPhone(phone);
                request.setAddress(address);

                companyService.updateCompany(id, request);

                apiResponse.setSuccess(true);
                apiResponse.setMessage("Company updated successfully");
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
            value = "company-delete",
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

        if (!role().canDeleteCompany()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authorized to delete company");
            return ERROR;
        }

        companyService.deleteCompany(id);

        apiResponse.setSuccess(true);
        apiResponse.setMessage("Company deleted successfully");
        apiResponse.setData(null);
        return SUCCESS;
    }
}
