package com.viswa.crm.api.auth;

import com.viswa.crm.dto.auth.LoginRequest;
import com.viswa.crm.dto.auth.LoginResponse;
import com.viswa.crm.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;


// Tried with Spring Rest Controller
@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private static final String USER_SESSION_KEY = "LOGGED_IN_USER";

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpSession session
    ) {
        try {
            LoginResponse response = authService.login(request);
            System.out.println("Login Response: " + response);

            // store in session
            session.setAttribute(USER_SESSION_KEY, response);

            System.out.println("Session attribute: " + USER_SESSION_KEY);

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> currentUser(HttpSession session) {

        LoginResponse user =
                (LoginResponse) session.getAttribute(USER_SESSION_KEY);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Not authenticated"));
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok(
                Map.of("message", "Logged out successfully")
        );
    }

    @GetMapping("/ping")
    public String ping() {
        return "API OK";
    }

}
