package edu.usta.groccy.dto.production;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductionReportRequest(

        @NotNull(message = "El ID de la ficha de producción es obligatorio")
        Long productionSheetId,

        @NotNull(message = "La cantidad completada es obligatoria")
        @Positive(message = "La cantidad debe ser positiva")
        Integer completedQuantity,

        String observations
) {}