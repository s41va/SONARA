package com.dawm.sonara.dtos.concierto;

import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.entities.Localidad;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConciertoCreateDTO {

    private Long id;

    @NotNull(message = "{msg.concierto.artista.notNull}")
    private Long artistaId;

    @NotNull(message = "{msg.concierto.localidad.notNull}")
    private Long localidadId;

    @NotNull(message = "{msg.concierto.fechaHora.notNull}")
    private LocalDateTime fechaHora;

    @NotBlank(message = "{msg.concierto.local.notEmpty}")
    private String local;

    @NotBlank(message = "{msg.concierto.descripcion.notEmpty}")
    @Size(max = 600, message = "{msg.concierto.descripcion.size}")
    private String descripcion;
}