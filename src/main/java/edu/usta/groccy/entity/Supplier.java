package edu.usta.groccy.entity;

import edu.usta.groccy.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(unique = true)
    private String email;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}