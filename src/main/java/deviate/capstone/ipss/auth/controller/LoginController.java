package deviate.capstone.ipss.auth.controller;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import deviate.capstone.ipss.auth.Dto.Requests.LoginRequest;
import deviate.capstone.ipss.auth.Dto.Responses.LoginResponse;
import deviate.capstone.ipss.auth.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class LoginController {
    
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = Objects.requireNonNull(authService);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> validateLoginAttempt(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.validateLoginAttempt(request));
    }
}
