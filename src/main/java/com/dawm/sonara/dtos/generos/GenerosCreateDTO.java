package com.dawm.sonara.dtos.generos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerosCreateDTO {


    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    private String descripcion;

}
