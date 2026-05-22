package edu.usta.groccy.controller;

import edu.usta.groccy.dto.auth.AuthResponse;
import edu.usta.groccy.dto.auth.AuthenticatedUserResponse;
import edu.usta.groccy.dto.auth.LoginRequest;
import edu.usta.groccy.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public AuthenticatedUserResponse getAuthenticatedUser() {
        return authService.getAuthenticatedUser();
    }
}