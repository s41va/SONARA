package com.dawm.sonara.services;

import com.dawm.sonara.dtos.perfil.UsuarioProfileDTO;
import com.dawm.sonara.dtos.perfil.UsuarioProfileUpdateDTO;

public interface UsuarioProfileService {
    UsuarioProfileDTO obtenerPerfilPorEmail(String email);
    UsuarioProfileDTO actualizarPerfil(String email, UsuarioProfileUpdateDTO dto);
}