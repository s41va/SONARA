package com.dawm.sonara.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistasUpdateDTO {


    private Long artista_id;

    @NotBlank
    @Size(max = 50)
    private String nombre_artistico;

    @NotBlank
    @Size(max = 100)
    private String pais;


    @Size(max = 400)
    private String descripcion;

}
