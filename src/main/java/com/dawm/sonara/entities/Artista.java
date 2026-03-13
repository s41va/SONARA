package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "artista")
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(name = "pais_origen", nullable = false, length = 100)
    private String pais;

    @Column(name = "descripcion", nullable = false, length = 400)
    private String descripcion;


    public Artista(String nombre_artistico, String pais, String descripcion) {
        this.nombre = nombre_artistico;
        this.pais = pais;
        this.descripcion = descripcion;
    }
}