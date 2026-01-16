package com.example.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "localidad")
public class Localidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "localidad_id", nullable = false, length = 100)
    private Long id;

    @Column(name = "pais", nullable = false, length = 100)
    private String pais;

    @Column(name = "nombre_ciudad", nullable = false, length = 100)
    private String nombreCiudad;

    @Column(name = "codigo_postal")
    private String codigoPostal;

}