package edu.usta.groccy.service;

import edu.usta.groccy.dto.auth.AuthResponse;
import edu.usta.groccy.dto.auth.AuthenticatedUserResponse;
import edu.usta.groccy.dto.auth.LoginRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthenticatedUserResponse getAuthenticatedUser();
}