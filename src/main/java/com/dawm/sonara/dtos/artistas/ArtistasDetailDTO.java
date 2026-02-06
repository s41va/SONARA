package com.dawm.sonara.dtos.artistas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistasDetailDTO {

    private Long artista_id;
    private String nombre_artistico;
    private String pais;
    private String descripcion;

}
