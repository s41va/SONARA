package com.dawm.sonara.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
