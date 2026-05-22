package edu.usta.groccy.dto.auth;

import edu.usta.groccy.enums.Role;

public record AuthenticatedUserResponse(
        Long id,
        String fullName,
        String email,
        Role role
) {
}