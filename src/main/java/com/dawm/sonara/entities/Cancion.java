package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Cancion")
public class Cancion {
    @Id
    @GeneratedValue
    @Column(name = "cancion_id", nullable = false)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id")
    private Artista artista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genero_id")
    private Genero genero;

    @Column(name = "fecha_lanzamiento", nullable = false)
    private LocalDate fecha_lanzamiento;

    @Column(name = "album", length = 200)
    private String album;
}
