package com.viswa.crm.action;

import com.viswa.crm.action.base.BaseAction;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.stereotype.Component;

import static com.opensymphony.xwork2.Action.SUCCESS;

@Component
@Action(
        value = "logout",
        results = {
                @Result(name = SUCCESS, location = "login.action", type = "redirect")
        }
)
public class LogoutAction extends BaseAction {

    @Override
    public String execute() {
        clearSession();
        return SUCCESS;
    }
}
