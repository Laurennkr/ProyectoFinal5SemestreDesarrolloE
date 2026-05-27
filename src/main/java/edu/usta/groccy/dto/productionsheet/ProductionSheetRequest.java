package edu.usta.groccy.dto.productionsheet;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public record ProductionSheetRequest(

        @NotBlank(message = "El título de la ficha es obligatorio")
        String title,

        @NotNull(message = "La cantidad solicitada es obligatoria")
        @Positive(message = "La cantidad debe ser positiva")
        Integer requestedQuantity,

        String referenceImageUrl,

        String cuttingGuide,

        @NotNull(message = "La fecha límite es obligatoria")
        @Future(message = "La fecha límite debe ser futura")
        LocalDate deadline,

        @NotNull(message = "El ID del producto es obligatorio")
        Long productId,

        @NotNull(message = "El ID del costurero asignado es obligatorio")
        Long assignedTailorId,

        @Valid
        List<RequiredMaterialRequest> requiredMaterials
) {}