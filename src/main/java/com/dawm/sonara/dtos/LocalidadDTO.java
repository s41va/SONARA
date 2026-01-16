package com.dawm.sonara.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalidadDTO {
    private Long id;
    private String pais;
    private String nombreCiudad;
    private String codigoPostal;
}
