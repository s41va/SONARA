package com.example.demo.mappers;

import com.example.demo.dtos.UsuarioCreateDTO;
import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.dtos.UsuarioDetailDTO;
import com.example.demo.dtos.UsuarioUpdateDTO;
import com.example.demo.entities.Usuario;

import java.util.List;

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

    public static void copyToExistingEntity(UsuarioUpdateDTO dto, Usuario entity) {
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
        // Localidad se debe setear en el service o controller
    }

}
