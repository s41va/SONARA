package com.dawm.sonara.dtos.localidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalidadCreateDTO {
    private Long id;

    @NotBlank(message = "{msg.localidad.pais.notEmpty}")
    @Size(max = 100, message = "{msg.localidad.pais.size}")
    private String pais;

    @NotBlank(message = "{msg.localidad.nombre_ciudad.notEmpty}")
    @Size(max = 100, message = "{msg.localidad.nombre_ciudad.size}")
    private String nombreCiudad;

    @NotNull(message = "{msg.localidad.codigo_postal.notNull}")
    @Size(max = 12, message = "{msg.localidad.codigo_postal.size}")
    @Pattern(regexp = "\\d+", message = "{msg.localidad.codigo_postal.pattern}") //tiene que ser string ya que puede empezar por 0
    private String codigoPostal;
}