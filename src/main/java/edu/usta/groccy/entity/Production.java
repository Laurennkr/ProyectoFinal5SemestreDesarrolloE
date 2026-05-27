package edu.usta.groccy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "productions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer completedQuantity;

    private String observations;

    @Column(nullable = false)
    private LocalDateTime reportedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_sheet_id", nullable = false)
    private ProductionSheet productionSheet;

    // Costurero que reporta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tailor_id", nullable = false)
    private User tailor;
}