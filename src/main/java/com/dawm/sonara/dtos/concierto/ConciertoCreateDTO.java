package com.dawm.sonara.dtos.concierto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConciertoCreateDTO {
    @NotBlank(message = "El ID del artista es obligatorio")
    private String artistaId; // String para la API

    @NotNull(message = "La localidad es obligatoria")
    private Long localidadId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El local es obligatorio")
    private String local;

    @Size(max = 600)
    private String descripcion;
}