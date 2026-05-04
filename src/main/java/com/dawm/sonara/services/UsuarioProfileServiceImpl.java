package com.dawm.sonara.services;

import com.dawm.sonara.dtos.perfil.UsuarioProfileDTO;
import com.dawm.sonara.entities.UsuarioPerfil;
import com.dawm.sonara.repositories.UsuarioProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioProfileServiceImpl implements UsuarioProfileService {

    @Autowired
    private UsuarioProfileRepository perfilRepository;

    @Override
    public UsuarioProfileDTO obtenerPerfilPorEmail(String email) {
        UsuarioPerfil perfil = perfilRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new RuntimeException("Perfil no encontrado para: " + email));

        // Convertimos la Entity a DTO (puedes usar un constructor en el DTO o un Mapper)
        return new UsuarioProfileDTO(
                perfil.getId(),
                perfil.getUsuario().getEmail(),
                perfil.getFirstName() + " " + perfil.getLastName(),
                perfil.getBio(),
                perfil.getPhoneNumber(),
                perfil.getProfileImage(),
                perfil.getCreatedAt()
        );
    }
}