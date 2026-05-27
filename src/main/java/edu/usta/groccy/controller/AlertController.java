package edu.usta.groccy.controller;

import edu.usta.groccy.dto.alert.AlertResponse;
import edu.usta.groccy.entity.Alert;
import edu.usta.groccy.enums.AlertStatus;
import edu.usta.groccy.exception.ResourceNotFoundException;
import edu.usta.groccy.mapper.AlertMapper;
import edu.usta.groccy.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alertas")
@RequiredArgsConstructor
public class AlertController {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AlertResponse>> findActive() {
        return ResponseEntity.ok(
                alertMapper.toResponseList(
                        alertRepository.findAllByStatus(AlertStatus.ACTIVE)));
    }

    @PatchMapping("/{id}/resolver")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertResponse> resolve(@PathVariable Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Alerta no encontrada con ID: " + id));
        alert.setStatus(AlertStatus.RESOLVED);
        return ResponseEntity.ok(alertMapper.toResponse(alertRepository.save(alert)));
    }
}