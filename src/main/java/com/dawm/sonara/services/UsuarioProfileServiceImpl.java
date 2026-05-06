package com.dawm.sonara.services;

import com.dawm.sonara.dtos.perfil.UsuarioProfileDTO;
import com.dawm.sonara.dtos.perfil.UsuarioProfileUpdateDTO;
import com.dawm.sonara.entities.*;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.UsuarioProfileMapper;
import com.dawm.sonara.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
public class UsuarioProfileServiceImpl implements UsuarioProfileService {

    @Autowired
    private UsuarioProfileRepository perfilRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Override
    @Transactional(readOnly = true)
    public UsuarioProfileDTO obtenerPerfilPorEmail(String email) {
        UsuarioPerfil perfil = perfilRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", "email", email));

        return UsuarioProfileMapper.toDTO(perfil);
    }

    @Override
    @Transactional
    public UsuarioProfileDTO actualizarPerfil(String email, UsuarioProfileUpdateDTO dto) {
        // 1. Obtener el perfil actual por el email del token
        UsuarioPerfil perfil = perfilRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", "email", email));

        Usuario usuario = perfil.getUsuario();

        // 2. Actualizar datos específicos del Perfil (usuario_profiles)
        perfil.setFirstName(dto.getFirstName());
        perfil.setLastName(dto.getLastName());
        perfil.setBio(dto.getBio());
        perfil.setPhoneNumber(dto.getPhoneNumber());
        perfil.setProfileImage(dto.getProfileImage());

        // 3. Actualizar datos de Preferencias en el Usuario (usuario)

        // Localidad
        if (dto.getLocalidadId() != null) {
            Localidad localidad = localidadRepository.findById(dto.getLocalidadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Localidad", "id", dto.getLocalidadId()));
            usuario.setLocalidad(localidad);
        }

        // Géneros Favoritos
        if (dto.getGenerosFavoritosIds() != null) {
            usuario.setGenerosFavoritos(new HashSet<>(generoRepository.findAllById(dto.getGenerosFavoritosIds())));
        }

        // Artistas Favoritos (Conversión de IDs a Entidades)
        if (dto.getArtistasFavoritosIds() != null) {
            // Buscamos en nuestra base de datos todos los artistas cuyos IDs coincidan con los del DTO
            // Esto es mucho más rápido que buscarlos uno por uno en un bucle
            usuario.setArtistasFavoritos(new HashSet<>(artistaRepository.findAllById(dto.getArtistasFavoritosIds())));
        }

        // Canciones Favoritas (Se mantiene igual porque siguen siendo Strings) (Sin uso todavia)
        if (dto.getCancionesFavoritasIds() != null) {
            usuario.setCancionesFavoritasIds(dto.getCancionesFavoritasIds());
        }

        // 4. Guardar. Debido al CascadeType.ALL en la entidad Usuario y al @Transactional,
        // con guardar el perfil (o dejar que Hibernate haga el dirty checking) es suficiente.
        UsuarioPerfil actualizado = perfilRepository.save(perfil);

        return UsuarioProfileMapper.toDTO(actualizado);
    }
}