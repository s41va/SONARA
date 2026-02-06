package com.dawm.sonara.dtos.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class UsuarioUpdateDTO {
    @NotNull(message = "{msg.usuario.id.notNull}")
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
