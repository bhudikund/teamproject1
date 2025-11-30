package ru.urfu.teamproject.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "active_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "is_soft", nullable = false)
    private boolean isSoft;

    @Column(name = "number_template")
    private String numberTemplate;
}