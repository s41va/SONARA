package com.dawm.sonara.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerosUpdateDTO {

    private Long id;
    @NotBlank
    @Size(max = 50)
    private String nombre;
    @Size(max = 400)
    private String descripcion;
}
