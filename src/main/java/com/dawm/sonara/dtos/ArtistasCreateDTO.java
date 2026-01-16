package com.dawm.sonara.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistasCreateDTO {

    private Long artista_id;

    @NotNull
    private String nombre_artistico;
    @NotNull
    private String pais;
    @NotNull
    private String descripcion;
}
