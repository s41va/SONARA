package com.dawm.sonara.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class InformacionPagoId implements Serializable {
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "concierto_id")
    private Long conciertoId;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
}