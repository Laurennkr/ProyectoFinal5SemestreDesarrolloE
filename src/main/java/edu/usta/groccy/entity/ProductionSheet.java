package edu.usta.groccy.entity;

import edu.usta.groccy.enums.ProductionSheetStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "production_sheets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer requestedQuantity;

    private String referenceImageUrl;

    @Column(columnDefinition = "TEXT")
    private String cuttingGuide;

    @Column(nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductionSheetStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Costurero asignado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_tailor_id", nullable = false)
    private User assignedTailor;

    @OneToMany(mappedBy = "productionSheet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RequiredMaterial> requiredMaterials = new ArrayList<>();
}