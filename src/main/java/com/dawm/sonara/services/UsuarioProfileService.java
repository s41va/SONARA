package com.dawm.sonara.services;

import com.dawm.sonara.dtos.perfil.UsuarioProfileDTO;

public interface UsuarioProfileService {
    UsuarioProfileDTO obtenerPerfilPorEmail(String email);
}
