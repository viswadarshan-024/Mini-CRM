package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.auth.LoginRequest;
import com.viswa.crm.dto.auth.LoginResponse;
import com.viswa.crm.service.AuthService;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;

@Getter
@Setter
@Component
@Action(
        value = "login",
        results = {
                @Result(name = INPUT, location = "login.jsp"),
                @Result(name = SUCCESS, location = "dashboard.action", type = "redirect")
        }
)

public class LoginAction extends BaseAction {

    private String username;
    private String password;

    @Autowired
    private AuthService authService;

    @Override
    public String execute() {

        // just show login page
        if (username == null && password == null) {
            return INPUT;
        }

        try {
            LoginRequest request = new LoginRequest();
            request.setUsername(username);
            request.setPassword(password);

            LoginResponse response = authService.login(request);

            // store user in session via BaseAction constant
            session.put(USER_SESSION_KEY, response);
            return SUCCESS;

        } catch (Exception ex) {
            addActionError("Invalid username or password");
            return INPUT;
        }
    }
}