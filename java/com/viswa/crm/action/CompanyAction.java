package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.company.CompanyResponse;
import com.viswa.crm.dto.company.CreateCompanyRequest;
import com.viswa.crm.dto.company.UpdateCompanyRequest;
import com.viswa.crm.service.CompanyService;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.opensymphony.xwork2.Action.*;

@AllowedMethods({"execute", "add", "edit", "save", "delete"})

@Component
@Action(
        value = "company",
        results = {
                @Result(name = SUCCESS, location = "company/company-list.jsp"),
                @Result(name = INPUT, location = "company/company-form.jsp"),
                @Result(name = LOGIN, location = "login.action", type = "redirect"),
                @Result(name = ERROR, location = "errors/403.jsp")
        }
)
@Getter
@Setter
public class CompanyAction extends BaseAction {

    @Autowired
    private CompanyService companyService;

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String keyword;

    private List<CompanyResponse> companies;

    @Override
    public String execute() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        if (keyword != null && !keyword.isBlank()) {
            companies = companyService.searchCompanies(keyword);
        } else {
            companies = companyService.getAllCompanies();
        }

        return SUCCESS;
    }

    public String add() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        if (!role().canCreateCompany()) return ERROR;

        clearForm();
        return INPUT;
    }

    public String edit() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        // Admin, Manager, Sales can edit
        if (!role().canEditCompany()) return ERROR;

        CompanyResponse company = companyService.getCompanyById(id);

        this.id = company.getId();
        this.name = company.getName();
        this.email = company.getEmail();
        this.phone = company.getPhone();
        this.address = company.getAddress();

        return INPUT;
    }

    public String save() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        try {
            if (id == null) {
                // CREATE
                if (!role().canCreateCompany()) return ERROR;

                CreateCompanyRequest request = new CreateCompanyRequest();
                request.setName(name);
                request.setEmail(email);
                request.setPhone(phone);
                request.setAddress(address);

                companyService.createCompany(request);

            } else {
                // UPDATE
                if (!role().canEditCompany()) return ERROR;

                UpdateCompanyRequest request = new UpdateCompanyRequest();
                request.setName(name);
                request.setEmail(email);
                request.setPhone(phone);
                request.setAddress(address);

                companyService.updateCompany(id, request);
            }

            return SUCCESS;

        } catch (Exception ex) {
            addActionError(ex.getMessage());
            return INPUT;
        }
    }

    public String delete() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        if (!role().canDeleteCompany()) return ERROR;

        companyService.deleteCompany(id);
        return SUCCESS;
    }

    private void clearForm() {
        this.id = null;
        this.name = null;
        this.email = null;
        this.phone = null;
        this.address = null;
    }
}
