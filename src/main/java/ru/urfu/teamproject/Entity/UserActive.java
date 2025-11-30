package ru.urfu.teamproject.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users_actives")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "activeid")
    private Asset active;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userid")
    private User user;

    @Column(name = "isapproved", nullable = false)
    private boolean isApproved;
}