package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "artista")
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artista_id;

    @Column(name = "nombre_artistico", nullable = false, unique = true, length = 50)
    private String nombre_artistico;

    @Column(name = "pais_origen",nullable = false, length = 100)
    private String pais;

    @Column(name = "descripcion", nullable = false, length = 400)
    private String descripcion;


    public Artista(String nombre_artistico, String pais, String descripcion) {
        this.nombre_artistico = nombre_artistico;
        this.pais = pais;
        this.descripcion = descripcion;
    }
}
