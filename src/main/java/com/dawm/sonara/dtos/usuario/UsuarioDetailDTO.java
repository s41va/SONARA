package com.dawm.sonara.dtos.usuario;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDetailDTO {
    private Long id;
    private String nombre;
    private String email;
    private String contrasena;
    private LocalDate fechaNacimiento;
    private Set<String> generosFavoritos;
    private String localidadNombre;
    private LocalDateTime fechaRegistro;

    private Set<String> roles;

}
