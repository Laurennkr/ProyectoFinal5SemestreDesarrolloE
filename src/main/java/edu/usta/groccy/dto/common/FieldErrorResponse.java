package edu.usta.groccy.dto.common;

public record FieldErrorResponse(
        String field,
        String message
) {
}