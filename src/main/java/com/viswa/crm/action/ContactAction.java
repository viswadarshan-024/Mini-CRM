package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.contact.ContactResponse;
import com.viswa.crm.dto.contact.CreateContactRequest;
import com.viswa.crm.dto.contact.UpdateContactRequest;
import com.viswa.crm.service.ContactService;
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
        value = "contact",
        results = {
                @Result(name = SUCCESS, location = "contact/contact-list.jsp"),
                @Result(name = INPUT, location = "contact/contact-form.jsp"),
                @Result(name = LOGIN, location = "login.action", type = "redirect"),
                @Result(name = ERROR, location = "errors/403.jsp")
        }
)
@Getter
@Setter
public class ContactAction extends BaseAction {

    @Autowired
    private ContactService contactService;

    private Long id;
    private Long companyId;
    private String name;
    private String email;
    private String phone;
    private String jobTitle;
    private String keyword;

    private List<ContactResponse> contacts;

    @Override
    public String execute() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        if (keyword != null && !keyword.isBlank()) {
            contacts = contactService.searchContacts(keyword);
        } else if (companyId != null) {
            contacts = contactService.getContactsByCompany(companyId);
        } else {
            contacts = contactService.searchContacts("");
        }

        return SUCCESS;
    }

    public String add() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        if (!role().canCreateContact()) return ERROR;

        clearForm();
        return INPUT;
    }
    public String edit() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        // All roles can edit contacts
        if (!role().canEditContact()) return ERROR;

        ContactResponse contact = contactService.getContactById(id);

        this.id = contact.getId();
        this.companyId = contact.getCompanyId();
        this.name = contact.getName();
        this.email = contact.getEmail();
        this.phone = contact.getPhone();
        this.jobTitle = contact.getJobTitle();

        return INPUT;
    }

    public String save() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        try {
            if (id == null) {

                if (!role().canCreateContact()) return ERROR;

                CreateContactRequest request = new CreateContactRequest();
                request.setCompanyId(companyId);
                request.setName(name);
                request.setEmail(email);
                request.setPhone(phone);
                request.setJobTitle(jobTitle);

                contactService.createContact(request);

            } else {

                if (!role().canEditContact()) return ERROR;

                UpdateContactRequest request = new UpdateContactRequest();
                request.setName(name);
                request.setEmail(email);
                request.setPhone(phone);
                request.setJobTitle(jobTitle);

                contactService.updateContact(id, request);
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

        if (!role().canDeleteContact()) return ERROR;

        contactService.deleteContact(id);
        return SUCCESS;
    }
    private void clearForm() {
        this.id = null;
        this.companyId = null;
        this.name = null;
        this.email = null;
        this.phone = null;
        this.jobTitle = null;
    }
}
