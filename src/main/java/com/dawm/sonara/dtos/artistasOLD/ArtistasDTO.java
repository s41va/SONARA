package com.dawm.sonara.dtos.artistasOLD;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ArtistasDTO {



    private Long id;

    @NotEmpty(message = "{msg.artista.nombre.notEmpty}")
    @Size(max = 50)
    private String nombre;

    @NotEmpty(message = "{msg.artista.pais.notEmpty}")
    @Size(max = 100)
    private String pais;


    @Size(max = 400)
    private String descripcion;


    //private GeneroSimpleDTO genero;
}
