package com.dawm.sonara.dtos.artistas;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ArtistasDTO {


    private Long artista_id;

    @NotEmpty
    @Size(max = 50)
    private String nombre_artistico;

    @NotEmpty
    @Size(max = 100)
    private String pais;


    @Size(max = 400)
    private String descripcion;

}
