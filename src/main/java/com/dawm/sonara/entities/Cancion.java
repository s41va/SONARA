package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Cancion")
public class Cancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cancion_id", nullable = false)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    // --- CAMBIO AQUÍ: De artista único a lista de artistas ---
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Cancion_Artista", // Nombre de la tabla intermedia en la DB
            joinColumns = @JoinColumn(name = "cancion_id"),
            inverseJoinColumns = @JoinColumn(name = "artista_id")
    )
    private List<Artista> artistas = new ArrayList<>();
    // ---------------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genero_id")
    private Genero genero;

    @Column(name = "fecha_lanzamiento", nullable = false)
    private LocalDate fecha_lanzamiento;

    @Column(name = "album", length = 200)
    private String album;
}
