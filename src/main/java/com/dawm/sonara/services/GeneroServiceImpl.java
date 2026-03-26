package com.dawm.sonara.services;


import com.dawm.sonara.dtos.generos.GenerosCreateDTO;
import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.dtos.generos.GenerosDetailDTO;
import com.dawm.sonara.dtos.generos.GenerosUpdateDTO;
import com.dawm.sonara.entities.Genero;
import com.dawm.sonara.exceptions.DuplicateResourceException;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.GeneroMapper;
import com.dawm.sonara.repositories.GeneroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Importante añadir la anotación para que Spring lo detecte
@Transactional
public class GeneroServiceImpl implements GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    @Override
    public Page<GenerosDTO> list(Pageable pageable) {
        // Obtenemos la página de entidades y mapeamos cada una a DTO
        return generoRepository.findAll(pageable).map(GeneroMapper::toDTO);
    }

    @Override
    public GenerosUpdateDTO getForEdit(Long id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("genero", "id", id));
        return GeneroMapper.toUpdateDTO(genero);
    }

    @Override
    public GenerosDTO create(GenerosCreateDTO dto) {
        // Suponiendo que el género tiene un campo 'nombre' que debe ser único
        if (generoRepository.existsByNombre(dto.getNombre())) {
            throw new DuplicateResourceException("genero", "nombre", dto.getNombre());
        }
        Genero genero = GeneroMapper.toEntity(dto);
        genero=generoRepository.save(genero);
        return GeneroMapper.toDTO(genero);
    }

    @Override
    public GenerosDTO update(GenerosUpdateDTO dto) {
        // Validamos si el nuevo nombre ya lo tiene otro registro (excluyendo el actual)
        if (generoRepository.existsByNombreAndIdNot(dto.getNombre(), dto.getId())) {
            throw new DuplicateResourceException("genero", "nombre", dto.getNombre());
        }

        Genero genero = generoRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("genero", "id", dto.getId()));

        // Copiamos los cambios del DTO a la entidad gestionada por JPA
        GeneroMapper.copyToExistingEntity(dto, genero);
        genero = generoRepository.save(genero);
        return GeneroMapper.toDTO(genero);
    }

    @Override
    public void delete(Long id) {
        if (!generoRepository.existsById(id)) {
            throw new ResourceNotFoundException("genero", "id", id);
        }
        // Nota: Si hay artistas asociados a este género, esto podría lanzar una excepción de
        // integridad referencial dependiendo de tu configuración de base de datos.
        generoRepository.deleteById(id);
    }

    @Override
    public GenerosDetailDTO getDetail(Long id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("genero", "id", id));
        return GeneroMapper.toDetailDTO(genero);
    }

    @Override
    public List<Genero> findAllGeneros() {
        // Reutilizamos el método para obtener la lista plana (para los combos select)
        return generoRepository.findAll();
    }
}