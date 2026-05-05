package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.perfil.UsuarioProfileDTO;
import com.dawm.sonara.dtos.perfil.UsuarioProfileUpdateDTO;
import com.dawm.sonara.services.UsuarioProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UsuarioProfileController {

    @Autowired
    private UsuarioProfileService perfilService;

    /**
     * Obtiene los datos del perfil del usuario autenticado.
     */
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioProfileDTO> getMiPerfil(Authentication authentication) {
        // authentication.getName() suele ser el email si así está configurado en tu UserDetailsService
        UsuarioProfileDTO dto = perfilService.obtenerPerfilPorEmail(authentication.getName());
        return ResponseEntity.ok(dto);
    }

    /**
     * Actualiza los datos del perfil y las preferencias del usuario autenticado.
     */
    @PutMapping("/perfil")
    public ResponseEntity<UsuarioProfileDTO> updateMiPerfil(
            Authentication authentication,
            @Valid @RequestBody UsuarioProfileUpdateDTO updateDTO) {

        // Usamos el email del token para asegurar que el usuario solo edite su propio perfil
        UsuarioProfileDTO actualizado = perfilService.actualizarPerfil(authentication.getName(), updateDTO);
        return ResponseEntity.ok(actualizado);
    }
}