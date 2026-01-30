package com.dawm.sonara.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RankingId implements Serializable {
    @Column(name = "localidad_id")
    private Long localidadId;

    @Column(name = "cancion_id")
    private Long cancionId;

    @Column(name = "fecha")
    private LocalDate fecha;
}