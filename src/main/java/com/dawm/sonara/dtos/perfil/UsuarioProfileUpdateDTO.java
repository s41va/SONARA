package com.dawm.sonara.dtos.perfil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UsuarioProfileUpdateDTO {
    // Campos del Perfil
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 500)
    private String bio;

    @Size(max = 30)
    private String phoneNumber;

    private String profileImage;

    // Campos de Preferencias (Usuario)
    private Long localidadId;
    private Set<Long> generosFavoritosIds;
    private Set<String> artistasFavoritosIds;
    private Set<String> cancionesFavoritasIds;
}