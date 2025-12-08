package com.viswa.crm.action.base;

import com.opensymphony.xwork2.ActionSupport;
import com.viswa.crm.dto.auth.LoginResponse;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public abstract class BaseAction extends ActionSupport implements SessionAware {

    protected Map<String, Object> session;

    public static final String USER_SESSION_KEY = "LOGGED_IN_USER";

    @Override
    public void setSession(Map<String, Object> session) {
        System.out.println(session);
        this.session = session;
        System.out.println("Role: " + isAdmin());
    }

    public LoginResponse getCurrentUser() {
        return (LoginResponse) session.get(USER_SESSION_KEY);
    }

    public boolean isAuthenticated() {
        return getCurrentUser() != null;
    }

    // role detection
    public boolean isAdmin() {
        return isAuthenticated() &&
                "ADMIN".equalsIgnoreCase(getCurrentUser().getRoleName());
    }

    public boolean isManager() {
        return isAuthenticated() &&
                "MANAGER".equalsIgnoreCase(getCurrentUser().getRoleName());
    }

    public boolean isSales() {
        return isAuthenticated() &&
                "SALES".equalsIgnoreCase(getCurrentUser().getRoleName());
    }

    public void clearSession() {
        if (session != null) {
            session.clear();
        }
    }
}
