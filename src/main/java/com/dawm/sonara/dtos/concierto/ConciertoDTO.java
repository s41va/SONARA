package com.dawm.sonara.dtos.concierto;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConciertoDTO {

    private Long id;
    private ArtistaDTO artista;
    private LocalidadDTO localidad;
    private LocalDateTime fechaHora;
    private String local;
    private String descripcion;
}