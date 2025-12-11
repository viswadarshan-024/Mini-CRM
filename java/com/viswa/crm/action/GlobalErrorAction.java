package com.viswa.crm.action;

import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import org.apache.struts2.convention.annotation.Action;

@Action(
        value = "global-error",
        results = {
                @org.apache.struts2.convention.annotation.Result(name = "success", location = "errors/error.jsp")
        }
)
@Getter
public class GlobalErrorAction extends ActionSupport {

    private String message;

    @Override
    public String execute() {
        return SUCCESS;
    }

    public void setException(Exception e) {
        this.message = e.getMessage();
    }
}
