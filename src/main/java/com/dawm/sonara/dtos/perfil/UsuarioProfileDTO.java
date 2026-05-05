package com.dawm.sonara.dtos.perfil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioProfileDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String nombreCompleto; // Calculado: firstName + lastName
    private String bio;
    private String phoneNumber;
    private String profileImage;
    private String locale;

    // Datos de la entidad Usuario (Aplanados)
    private String localidadNombre;
    private Set<String> generosFavoritos;
    private Set<String> artistasFavoritosIds;
    private Set<String> cancionesFavoritasIds;

    private LocalDateTime fechaRegistro;
}