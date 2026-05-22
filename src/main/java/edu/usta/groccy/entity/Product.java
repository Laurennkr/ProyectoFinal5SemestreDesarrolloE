package edu.usta.groccy.entity;

import edu.usta.groccy.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String color;

    private String category;

    @Column(nullable = false)
    private BigDecimal salePrice;

    @Column(nullable = false)
    private BigDecimal productionCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}