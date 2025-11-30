package ru.urfu.teamproject.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ФИО одной строкой
    @Column(name = "name")
    private String fullName;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String passwordHash; // md5

    @Column(name = "is_work", nullable = false)
    private boolean isWork = true;

    // Остальные поля по диаграмме (можешь добавить при необходимости)
    @Column(name = "middlename")
    private String middleName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "companyid")
    private Long companyId;

    @ManyToOne
    @JoinColumn(name = "parentuserid")
    private User parentUser;
}