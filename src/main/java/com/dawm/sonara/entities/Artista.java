package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artista")
public class Artista {

    @Id
    private Integer id; // Usaremos el idArtist de la API

    private String nombre;

    @Column(name = "genero_id")
    private Integer generoId;

    @Column(name = "votos_ranking")
    private Integer votosRanking = 0;

    // Campos temporales (No se guardan en la DB local, solo para la API)
    @Transient
    private String biografia;
    @Transient
    private String foto;
    @Transient
    private String web;
}