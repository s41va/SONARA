package com.dawm.sonara.dtos.concierto;

import com.dawm.sonara.dtos.artistasOLD.ArtistasDTO;
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
    private ArtistasDTO artista;
    private LocalidadDTO localidad;
    private LocalDateTime fechaHora;
    private String local;
    private String descripcion;
}