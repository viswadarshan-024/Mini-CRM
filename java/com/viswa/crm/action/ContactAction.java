package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.common.ApiResponse;
import com.viswa.crm.dto.contact.ContactResponse;
import com.viswa.crm.dto.contact.CreateContactRequest;
import com.viswa.crm.dto.contact.UpdateContactRequest;
import com.viswa.crm.service.ContactService;
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

@AllowedMethods({"execute", "add", "edit", "save", "delete"})
@Component
@Getter
@Setter
@ParentPackage("json-default")
@Namespace("/")
public class ContactAction extends BaseAction {

    @Autowired
    private transient ContactService contactService;  // do not serialize

    // JSON root
    private ApiResponse<Object> apiResponse = new ApiResponse<>();

    // Request / filter fields
    private Long id;
    private Long companyId;
    private String name;
    private String email;
    private String phone;
    private String jobTitle;
    private String keyword;

    // ===== LIST / SEARCH =====
    @Action(
            value = "contact",
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

        List<ContactResponse> contacts;
        if (keyword != null && !keyword.isBlank()) {
            contacts = contactService.searchContacts(keyword);
            apiResponse.setMessage("Contacts searched successfully");
        } else if (companyId != null) {
            contacts = contactService.getContactsByCompany(companyId);
            apiResponse.setMessage("Contacts for company fetched successfully");
        } else {
            contacts = contactService.searchContacts("");
            apiResponse.setMessage("Contacts fetched successfully");
        }

        apiResponse.setSuccess(true);
        apiResponse.setData(contacts);
        return SUCCESS;
    }

    // ===== PREPARE ADD =====
    @Action(
            value = "contact-add",
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

        if (!role().canCreateContact()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authorized to create contact");
            return ERROR;
        }

        apiResponse.setSuccess(true);
        apiResponse.setMessage("Ready to create contact");
        apiResponse.setData(null);
        return SUCCESS;
    }

    // ===== GET ONE FOR EDIT =====
    @Action(
            value = "contact-edit",
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

        if (!role().canEditContact()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authorized to edit contact");
            return ERROR;
        }

        ContactResponse contact = contactService.getContactById(id);

        apiResponse.setSuccess(true);
        apiResponse.setMessage("Contact fetched successfully");
        apiResponse.setData(contact);
        return SUCCESS;
    }

    // ===== CREATE / UPDATE =====
    @Action(
            value = "contact-save",
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
                if (!role().canCreateContact()) {
                    apiResponse.setSuccess(false);
                    apiResponse.setMessage("Not authorized to create contact");
                    return ERROR;
                }

                CreateContactRequest request = new CreateContactRequest();
                request.setCompanyId(companyId);
                request.setName(name);
                request.setEmail(email);
                request.setPhone(phone);
                request.setJobTitle(jobTitle);

                contactService.createContact(request);

                apiResponse.setSuccess(true);
                apiResponse.setMessage("Contact created successfully");
                apiResponse.setData(null);

            } else {
                if (!role().canEditContact()) {
                    apiResponse.setSuccess(false);
                    apiResponse.setMessage("Not authorized to update contact");
                    return ERROR;
                }

                UpdateContactRequest request = new UpdateContactRequest();
                request.setName(name);
                request.setEmail(email);
                request.setPhone(phone);
                request.setJobTitle(jobTitle);

                contactService.updateContact(id, request);

                apiResponse.setSuccess(true);
                apiResponse.setMessage("Contact updated successfully");
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
            value = "contact-delete",
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

        if (!role().canDeleteContact()) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Not authorized to delete contact");
            return ERROR;
        }

        try {
            contactService.deleteContact(id);
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Contact deleted successfully");
            apiResponse.setData(null);
            return SUCCESS;

        } catch (RuntimeException ex) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage(ex.getMessage());
            apiResponse.setData(null);
            return ERROR;
        }
    }
}
