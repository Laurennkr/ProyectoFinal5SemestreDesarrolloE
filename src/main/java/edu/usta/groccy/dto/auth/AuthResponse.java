package edu.usta.groccy.dto.auth;

import edu.usta.groccy.enums.Role;

public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String fullName,
        String email,
        Role role
) {
}