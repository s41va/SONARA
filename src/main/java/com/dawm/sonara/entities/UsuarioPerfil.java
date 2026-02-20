package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "usuario_perfil")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfil {

    @Id
    @Column(name = "usuario_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "locale", length = 10)
    private String locale;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
