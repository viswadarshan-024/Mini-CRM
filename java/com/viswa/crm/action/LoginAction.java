package com.viswa.crm.action;
import com.viswa.crm.action.base.BaseAction;
import com.viswa.crm.dto.auth.LoginRequest;
import com.viswa.crm.dto.auth.LoginResponse;
import com.viswa.crm.service.AuthService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class LoginAction extends BaseAction {

    private String username;
    private String password;
    private LoginResponse responseData;
    private String errorMessage;

    @Autowired
    private transient AuthService authService;

    @Override
    public String execute() {
        try {
            System.out.println("LoginAttempt: " + username);

            LoginRequest request = new LoginRequest();
            request.setUsername(username);
            request.setPassword(password);

            LoginResponse response = authService.login(request);
            session.put(USER_SESSION_KEY, response);

            this.responseData = response;
            return SUCCESS;

        } catch (Exception ex) {
            this.errorMessage = "Invalid username or password";
            return ERROR;
        }
    }
}
