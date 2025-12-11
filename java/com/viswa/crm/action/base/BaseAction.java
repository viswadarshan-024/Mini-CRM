package com.viswa.crm.action.base;

import com.opensymphony.xwork2.ActionSupport;
import com.viswa.crm.dto.auth.LoginResponse;
import com.viswa.crm.security.strategy.RoleStrategy;
import com.viswa.crm.security.strategy.RoleStrategyProvider;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

// This application follows SOLID principles
// And all necessary Design Patterns
// MVC pattern
public abstract class BaseAction extends ActionSupport implements SessionAware {

    protected Map<String, Object> session;

    public static final String USER_SESSION_KEY = "LOGGED_IN_USER";

    @Autowired
    private RoleStrategyProvider roleStrategyProvider;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public LoginResponse getCurrentUser() {
        return (LoginResponse) session.get(USER_SESSION_KEY);
    }

    public boolean isAuthenticated() {
        return getCurrentUser() != null;
    }

    protected RoleStrategy role() {
        if (!isAuthenticated()) {
            throw new RuntimeException("Not authenticated");
        }
        return roleStrategyProvider.getStrategy(
                getCurrentUser().getRoleName()
        );
    }

    protected void clearSession() {
        if (session != null) {
            session.clear();
        }
    }
}


// Need this for testing purpose

//public abstract class BaseAction extends ActionSupport implements SessionAware {
//
//    protected Map<String, Object> session;
//
//    public static final String USER_SESSION_KEY = "LOGGED_IN_USER";
//
//    @Override
//    public void setSession(Map<String, Object> session) {
//        System.out.println(session);
//        this.session = session;
//        System.out.println("Role: " + isAdmin());
//    }
//
//    public LoginResponse getCurrentUser() {
//        return (LoginResponse) session.get(USER_SESSION_KEY);
//    }
//
//    public boolean isAuthenticated() {
//        return getCurrentUser() != null;
//    }
//
//    // role detection
//    public boolean isAdmin() {
//        return isAuthenticated() &&
//                "ADMIN".equalsIgnoreCase(getCurrentUser().getRoleName());
//    }
//
//    public boolean isManager() {
//        return isAuthenticated() &&
//                "MANAGER".equalsIgnoreCase(getCurrentUser().getRoleName());
//    }
//
//    public boolean isSales() {
//        return isAuthenticated() &&
//                "SALES".equalsIgnoreCase(getCurrentUser().getRoleName());
//    }
//
//    public void clearSession() {
//        if (session != null) {
//            session.clear();
//        }
//    }
//}
