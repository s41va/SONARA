package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.usuario.UsuarioCreateDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDetailDTO;
import com.dawm.sonara.dtos.usuario.UsuarioUpdateDTO;
import com.dawm.sonara.entities.Roles;
import com.dawm.sonara.entities.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class UsuarioMapper {
    // Entity -> DTO (listado/tabla básico)
    public static UsuarioDTO toDTO(Usuario entity) {
        if (entity == null) return null;
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setEmail(entity.getEmail());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        dto.setFechaRegistro(entity.getFechaRegistro());
        dto.setGenerosFavoritos(entity.getGenerosFavoritos());
        dto.setLocalidadNombre(entity.getLocalidad() != null ? entity.getLocalidad().getNombreCiudad() : null);
        return dto;
    }

    public static List<UsuarioDTO> toDTOList(List<Usuario> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(UsuarioMapper::toDTO).toList();
    }

    // Entity -> DTO detalle completo
    public static UsuarioDetailDTO toDetailDTO(Usuario entity) {
        if (entity == null) return null;
        UsuarioDetailDTO dto = new UsuarioDetailDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setEmail(entity.getEmail());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        dto.setFechaRegistro(entity.getFechaRegistro());
        dto.setGenerosFavoritos(entity.getGenerosFavoritos());
        dto.setLocalidadNombre(entity.getLocalidad() != null ? entity.getLocalidad().getNombreCiudad() : null);
        return dto;
    }

    public static UsuarioUpdateDTO toUpdateDTO(Usuario entity) {
        if (entity == null) return null;
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setEmail(entity.getEmail());
        dto.setFechaRegistro(entity.getFechaRegistro());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        dto.setGenerosFavoritos(entity.getGenerosFavoritos());
        dto.setLocalidadId(entity.getLocalidad() != null ? entity.getLocalidad().getId() : null);

        if (entity.getRoles() != null) {
            dto.setRolesIds(
                    entity.getRoles()
                            .stream()
                            .map(Roles::getId)
                            .collect(java.util.stream.Collectors.toSet())
            );
        }
        return dto;
    }

    // DTO (Create/Update) -> Entity
    public static Usuario toEntity(UsuarioCreateDTO dto) {
        if (dto == null) return null;
        Usuario e = new Usuario();
        e.setNombre(dto.getNombre());
        e.setEmail(dto.getEmail());
        e.setContrasena(dto.getContrasena());
        e.setFechaNacimiento(dto.getFechaNacimiento());
        e.setFechaRegistro(dto.getFechaRegistro());
        e.setGenerosFavoritos(dto.getGenerosFavoritos());
        // Localidad se debe setear en el service o controller con la entidad correspondiente
        return e;
    }

    public static Usuario toEntity(UsuarioUpdateDTO dto) {
        if (dto == null) return null;
        Usuario e = new Usuario();
        e.setId(dto.getId());
        e.setNombre(dto.getNombre());
        e.setEmail(dto.getEmail());
        e.setContrasena(dto.getContrasena());
        e.setFechaNacimiento(dto.getFechaNacimiento());
        e.setFechaRegistro(dto.getFechaRegistro());
        e.setGenerosFavoritos(dto.getGenerosFavoritos());
        // Localidad se debe setear en el service o controller con la entidad correspondiente
        return e;
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;
        Usuario e = new Usuario();
        e.setId(dto.getId());
        e.setNombre(dto.getNombre());
        e.setEmail(dto.getEmail());
        e.setFechaNacimiento(dto.getFechaNacimiento());
        e.setGenerosFavoritos(dto.getGenerosFavoritos());
        e.setFechaRegistro(dto.getFechaRegistro());
        // Localidad se debe setear en el service o controller
        return e;
    }

    public static void copyToExistingEntity(UsuarioUpdateDTO dto, Usuario entity, Set<Roles> roles) {
        if (dto == null || entity == null) return;
        entity.setNombre(dto.getNombre());
        entity.setEmail(dto.getEmail());
        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            entity.setContrasena(dto.getContrasena());
        }
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getFechaRegistro() != null) {
            entity.setFechaRegistro(dto.getFechaRegistro());
        }
        entity.setGenerosFavoritos(dto.getGenerosFavoritos());
        entity.setRoles(roles);
        // Localidad se debe setear en el service o controller
    }

    public static Usuario copyToNewEntity(UsuarioCreateDTO dto, Set<Roles> roles) {
        if (dto == null) return null;

        // Crear una nueva entidad Usuario
        Usuario entity = new Usuario();

        // Asignar datos del DTO al nuevo usuario
        entity.setNombre(dto.getNombre());
        entity.setEmail(dto.getEmail());

        // Validación de contraseña si está presente
        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            // Aquí podrías considerar encriptar la contraseña antes de asignarla
            entity.setContrasena(dto.getContrasena());
        }

        // Asignar la fecha de nacimiento
        entity.setFechaNacimiento(dto.getFechaNacimiento());

        // Establecer roles
        if (roles != null) {
            entity.setRoles(roles);
        }

        // Generar fecha de registro (automáticamente)
        entity.setFechaRegistro(LocalDateTime.now());

        // Otros campos adicionales del DTO
        entity.setGenerosFavoritos(dto.getGenerosFavoritos());

        return entity;
    }

}