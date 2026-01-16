package com.example.demo.mappers;
import com.example.demo.dtos.LocalidadCreateDTO;
import com.example.demo.dtos.LocalidadDTO;
import com.example.demo.dtos.LocalidadDetailDTO;
import com.example.demo.dtos.LocalidadUpdateDTO;
import com.example.demo.entities.Localidad;

import java.util.List;

public class LocalidadMapper {
    // Entity -> DTO (listado/tabla basico)
    public static LocalidadDTO toDTO(Localidad entity) {
        if (entity == null) return null;
        LocalidadDTO dto = new LocalidadDTO();
        dto.setId(entity.getId());
        dto.setNombreCiudad(entity.getNombreCiudad());
        dto.setPais(entity.getPais());
        //Establecemos solo el nombre de la region que es lo unico que mostramos en los listados
        dto.setCodigoPostal(entity.getCodigoPostal());
        return dto;
    }
    public static List<LocalidadDTO> toDTOList(List<Localidad> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(LocalidadMapper::toDTO).toList();
    }
    //Entity -> DTO (detalle con provincias)
    public static LocalidadDetailDTO toDetailDTO(Localidad entity) {
        if (entity == null) return null;
        LocalidadDetailDTO dto = new LocalidadDetailDTO();
        dto.setId(entity.getId());
        dto.setPais(entity.getPais());
        dto.setNombreCiudad(entity.getNombreCiudad());
        dto.setCodigoPostal(entity.getCodigoPostal());
        return dto;
    }
    public static LocalidadDTO toLocalidadDTO(Localidad p){
        if (p == null) return null;
        LocalidadDTO dto = new LocalidadDTO();
        dto.setId(p.getId());
        dto.setNombreCiudad(p.getNombreCiudad());
        dto.setCodigoPostal(p.getCodigoPostal());
        return dto;
    }
    public static List<LocalidadDTO> toLocalidadList(List<Localidad> provinces) {
        if (provinces == null) return List.of();
        return provinces.stream().map(LocalidadMapper::toLocalidadDTO).toList();
    }
    public static LocalidadUpdateDTO toUpdateDTO(Localidad entity) {
        if (entity == null) return null;
        LocalidadUpdateDTO dto = new LocalidadUpdateDTO();
        dto.setId(entity.getId());
        dto.setPais(entity.getPais());
        dto.setNombreCiudad(entity.getNombreCiudad());
        dto.setCodigoPostal(entity.getCodigoPostal());
        return dto;
    }
    //DTO (Create/Update) -> Entity
    public static Localidad toEntity(LocalidadUpdateDTO dto){
        if (dto == null) return null;
        Localidad e = new Localidad();
        e.setId(dto.getId());
        e.setPais(dto.getPais());
        e.setNombreCiudad(dto.getNombreCiudad());
        e.setCodigoPostal(dto.getCodigoPostal());
        return e;
    }
    public static Localidad toEntity(LocalidadCreateDTO dto){
        if (dto == null) return null;
        Localidad e = new Localidad();
        e.setId(dto.getId());
        e.setPais(dto.getPais());
        e.setNombreCiudad(dto.getNombreCiudad());
        e.setCodigoPostal(dto.getCodigoPostal());
        return e;
    }
    public static Localidad toEntity(LocalidadDTO dto){
        if (dto == null) return null;
        Localidad e = new Localidad();
        e.setId(dto.getId());
        e.setPais(dto.getPais());
        e.setNombreCiudad(dto.getNombreCiudad());
        e.setCodigoPostal(dto.getCodigoPostal());
        return e;
    }
    public static void copyToExistingEntity(LocalidadUpdateDTO dto, Localidad entity){
        if (dto == null || entity == null) return;
        entity.setPais(dto.getPais());
        entity.setNombreCiudad(dto.getNombreCiudad());
        entity.setCodigoPostal(dto.getCodigoPostal());
    }
}