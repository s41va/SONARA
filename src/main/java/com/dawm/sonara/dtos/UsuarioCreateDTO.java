package com.dawm.sonara.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateDTO {
    private Long id;

    @NotBlank(message = "{msg.usuario.nombre.notEmpty}")
    @Size(max = 100, message = "{msg.usuario.nombre.size}")
    private String nombre;

    @NotBlank(message = "{msg.usuario.email.notEmpty}")
    @Email(message = "{msg.usuario.email.valid}")
    private String email;

    @NotBlank(message = "{msg.usuario.contrasena.notEmpty}")
    @Size(min = 6, max = 100, message = "{msg.usuario.contrasena.size}")
    private String contrasena;

    @NotNull(message = "{msg.usuario.fechaNacimiento.notNull}")
    private LocalDate fechaNacimiento;

    @NotNull(message = "{msg.usuario.generosFavoritos.notNull}")
    private Set<@NotBlank(message = "{msg.usuario.generosFavoritos.notEmpty}") String> generosFavoritos;

    @NotNull(message = "{msg.usuario.localidadId.notNull}")
    private Long localidadId;

    private LocalDateTime fechaRegistro;

}
