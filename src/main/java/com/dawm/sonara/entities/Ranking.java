package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Ranking")
public class Ranking {

    @EmbeddedId
    private RankingId id;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("localidadId") // Enlaza con el campo del RankingId
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cancionId") // Enlaza con el campo del RankingId
    @JoinColumn(name = "cancion_id")
    private Cancion cancion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id")
    private Artista artista;

    @Column(name = "posicion")
    private Integer posicion;
}