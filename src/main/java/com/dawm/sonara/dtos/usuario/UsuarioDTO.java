package com.dawm.sonara.dtos.usuario;

import com.dawm.sonara.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String contrasenaHash;
    private LocalDate fechaNacimiento;
    private Set<String> generosFavoritos;
    private String localidadNombre;
    private LocalDateTime fechaRegistro;

    private Set<String> roles;

    private Set<String> artistasFavoritosIds;
    private Set<String> cancionesFavoritasIds;
}
