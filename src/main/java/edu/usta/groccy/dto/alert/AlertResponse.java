package edu.usta.groccy.dto.alert;

import edu.usta.groccy.enums.AlertStatus;
import edu.usta.groccy.enums.AlertType;

import java.time.LocalDateTime;

public record AlertResponse(
        Long id,
        AlertType type,
        String message,
        AlertStatus status,
        LocalDateTime createdAt,
        Long relatedEntityId,
        String relatedEntityType
) {}