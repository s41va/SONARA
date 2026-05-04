package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.perfil.UsuarioProfileDTO;
import com.dawm.sonara.entities.UsuarioPerfil;
import com.dawm.sonara.services.UsuarioProfileService;
import com.dawm.sonara.services.UsuarioProfileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioProfileController {

    @Autowired
    private UsuarioProfileService perfilService;

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioProfileDTO> getMiPerfil(Authentication authentication) {
        // authentication.getName() devuelve el 'sub' del JWT (normalmente el email)
        UsuarioProfileDTO dto = perfilService.obtenerPerfilPorEmail(authentication.getName());
        return ResponseEntity.ok(dto);
    }
}

