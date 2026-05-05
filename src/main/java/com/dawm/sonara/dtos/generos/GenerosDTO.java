package com.dawm.sonara.dtos.generos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GenerosDTO {

    private Long id;
    @NotEmpty(message = "msg.genero.nombre.notEmpty")
    @Size(message = "msg.genero.nombre.size", max = 50)
    private String nombre;
}
