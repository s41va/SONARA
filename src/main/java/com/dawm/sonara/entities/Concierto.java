package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conciertos") // Minúsculas es mejor práctica en DB
public class Concierto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Guardamos los datos clave de la API para no tener que consultarla siempre
    @Column(name = "artista_id")
    private String artistaId;

    @Column(name = "artista_nombre")
    private String artistaNombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    private String local;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock")
    private Integer stock;
}