package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cancion")
public class Cancion {

    @Id
    @Column(name = "cancion_id")
    private Integer cancionId; // El idTrack de la API

    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id")
    private Artista artista; // Relación real con tu tabla Artista

    @Column(name = "reproducciones_locales")
    private Integer reproduccionesLocales = 0;

    // Campos volátiles (solo de la API, no en DB)
    @Transient
    private String album;
    @Transient
    private String videoUrl;
    @Transient
    private String portada;
}