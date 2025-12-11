package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.auth.CreateUserRequest;
import com.viswa.crm.dto.auth.UserResponse;
import com.viswa.crm.service.AuthService;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.opensymphony.xwork2.Action.*;

@AllowedMethods({"execute", "add", "edit", "save", "delete", "view"})

@Component
@Action(
        value = "user",
        results = {
                @Result(name = SUCCESS, location = "admin/user-list.jsp"),
                @Result(name = INPUT, location = "admin/user-form.jsp"),
                @Result(name = LOGIN, location = "login.action", type = "redirect"),
                @Result(name = ERROR, location = "errors/403.jsp")
        }
)
@Getter
@Setter
public class UserAction extends BaseAction {

    @Autowired
    private AuthService authService;

    // form fields
    private Long id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private Long roleId;

    // ui data
    private List<UserResponse> users;
    private UserResponse user;
    private Map<Long, String> roleOptions;

    @Override
    public String execute() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        if (!role().canManageUsers()) {
            return ERROR;
        }

        users = authService.getAllUsers();
        return SUCCESS;
    }

    public String add() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        if (!role().canManageUsers()) {
            return ERROR;
        }

        roleOptions = new LinkedHashMap<>();
        roleOptions.put(1L, "ADMIN");
        roleOptions.put(2L, "MANAGER");
        roleOptions.put(3L, "SALES");

        clearForm();
        return INPUT;
    }

    public String save() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        if (!role().canManageUsers()) {
            return ERROR;
        }

        try {
            CreateUserRequest request = new CreateUserRequest();
            request.setUsername(username);
            request.setEmail(email);
            request.setPassword(password);
            request.setFullName(fullName);
            request.setRoleId(roleId);

            authService.createUser(request);
            return SUCCESS;

        } catch (Exception ex) {
            loadRoleOptions();
            addActionError(ex.getMessage());
            return INPUT;
        }
    }

    public String delete() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        if (!role().canManageUsers()) {
            return ERROR;
        }

        try {
            authService.deleteUser(id);
            return SUCCESS;
        } catch (Exception ex) {
            addActionError(ex.getMessage());
            return ERROR;
        }

    }

    public String view() {

        if (!isAuthenticated()) {
            return LOGIN;
        }

        if (!role().canManageUsers()) {
            return ERROR;
        }

        user = authService.getUserById(id);
        return SUCCESS;
    }



    private void clearForm() {
        this.id = null;
        this.username = null;
        this.email = null;
        this.password = null;
        this.fullName = null;
        this.roleId = null;
    }

    private void loadRoleOptions() {
        roleOptions = new LinkedHashMap<>();
        roleOptions.put(1L, "ADMIN");
        roleOptions.put(2L, "MANAGER");
        roleOptions.put(3L, "SALES");
    }
}
