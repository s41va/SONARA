package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.perfil.UsuarioProfileDTO;
import com.dawm.sonara.entities.Genero;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.entities.UsuarioPerfil;

import java.util.Set;
import java.util.stream.Collectors;

public class UsuarioProfileMapper {

    public static UsuarioProfileDTO toDTO(UsuarioPerfil entity) {
        if (entity == null) return null;

        Usuario user = entity.getUsuario();

        return UsuarioProfileDTO.builder()
                .id(entity.getId())
                .email(user.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .nombreCompleto(entity.getFirstName() + (entity.getLastName() != null ? " " + entity.getLastName() : ""))
                .bio(entity.getBio())
                .phoneNumber(entity.getPhoneNumber())
                .profileImage(entity.getProfileImage())
                .locale(entity.getLocale())
                .fechaRegistro(user.getFechaRegistro())
                // Mapeo de datos del Usuario
                .localidadNombre(user.getLocalidad() != null ? user.getLocalidad().getNombreCiudad() : null)
                .generosFavoritos(mapGeneros(user.getGenerosFavoritos()))
                .artistasFavoritosIds(user.getArtistasFavoritosIds())
                .cancionesFavoritasIds(user.getCancionesFavoritasIds())
                .build();
    }

    private static Set<String> mapGeneros(Set<Genero> generos) {
        if (generos == null) return Set.of();
        return generos.stream()
                .map(Genero::getNombre)
                .collect(Collectors.toSet());
    }
}