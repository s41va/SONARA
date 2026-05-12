package com.dawm.sonara.entities;

import com.dawm.sonara.entities.enums.Estado;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "solicitudes_artistas")
public class SolicitudArtista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreArtista;

    private String generoSugerido;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String fotoUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioSolicitante;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;

    private LocalDateTime fechaSolicitud = LocalDateTime.now();
}