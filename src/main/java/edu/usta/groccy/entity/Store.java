package edu.usta.groccy.entity;

import edu.usta.groccy.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    private String zone;

    private String type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}