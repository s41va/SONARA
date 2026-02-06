package com.dawm.sonara.dtos.generos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GenerosDTO {

    private Long id;
    private String nombre;
    private String descripcion;
}
