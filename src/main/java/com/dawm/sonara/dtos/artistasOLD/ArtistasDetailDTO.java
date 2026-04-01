package com.dawm.sonara.dtos.artistasOLD;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistasDetailDTO {

    private Long id;
    private String nombre;
    private String pais;
    private String descripcion;

}
