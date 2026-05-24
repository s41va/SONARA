package com.dawm.sonara.dtos.concierto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConciertoUpdateDTO {

    @NotNull(message = "{msg.concierto.id.notNull}")
    private Long id;

    @NotNull(message = "{msg.concierto.artista.notNull}")
    private String artistaId;

    @NotNull(message = "{msg.concierto.localidad.notNull}")
    private Long localidadId;

    @NotNull(message = "{msg.concierto.fechaHora.notNull}")
    private LocalDateTime fechaHora;

    @NotBlank(message = "{msg.concierto.local.notEmpty}")
    private String local;

    @NotBlank(message = "{msg.concierto.descripcion.notEmpty}")
    @Size(max = 600, message = "{msg.concierto.descripcion.size}")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    private BigDecimal precio;
}