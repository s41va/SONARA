package com.dawm.sonara.dtos.perfil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioProfileDTO {
    private Long id;
    private String email;
    private String nombreCompleto;
    private String bio;
    private String phoneNumber;
    private String profileImage;
    private LocalDateTime fechaRegistro;
}
