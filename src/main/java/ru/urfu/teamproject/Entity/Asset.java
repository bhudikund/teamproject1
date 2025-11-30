package ru.urfu.teamproject.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "actives")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "typeid")
    private AssetType typeObject;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "parentactiveid")
    private Asset parentActive;

    @Column(name = "uniquenumber", nullable = false, unique = true)
    private String inventoryNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "statusid")
    private AssetStatus status;

    @Column(name = "serialnumber")
    private String serialNumber;

    @Column(name = "bitrixcode")
    private String bitrixCode;

    @Column(name = "onescode")
    private String oneSCode;

    @Column(name = "address")
    private String address;

    // Этого поля нет на диаграмме, но оно нужно для API (date_create)
    @Column(name = "date_create", nullable = false)
    private LocalDateTime dateCreate;
}