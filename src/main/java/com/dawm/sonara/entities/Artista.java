package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artista")
public class Artista {

    @Id
    @Column(name = "id")
    private String id; // Usaremos el idArtist de la API

    private String nombre;

    private String genero;

    @Column(name = "ultima_sincronizacion")
    private LocalDateTime ultimaSincronizacion = LocalDateTime.now();

    @Column(name = "votos_ranking")
    private Integer votosRanking = 0;

    private String foto;

    // Campos temporales (No se guardan en la DB local, solo para la API)
    @Transient
    private String biografia;

    @Transient
    private String web;
}