package com.dawm.sonara.services;

import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.entities.Genero;
import com.dawm.sonara.exceptions.DuplicateResourceException;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.GeneroMapper;
import com.dawm.sonara.repositories.GeneroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GeneroServiceImpl implements GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    @Override
    public Page<GenerosDTO> list(Pageable pageable) {
        return generoRepository.findAll(pageable).map(GeneroMapper::toDTO);
    }

    @Override
    public GenerosDTO getDetail(Long id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("genero", "id", id));
        return GeneroMapper.toDTO(genero);
    }

    @Override
    public GenerosDTO create(GenerosDTO dto) {
        if (generoRepository.existsByNombre(dto.getNombre())) {
            throw new DuplicateResourceException("genero", "nombre", dto.getNombre());
        }
        Genero genero = GeneroMapper.toEntity(dto);
        genero = generoRepository.save(genero);
        return GeneroMapper.toDTO(genero);
    }

    @Override
    public GenerosDTO update(GenerosDTO dto) {
        if (generoRepository.existsByNombreAndIdNot(dto.getNombre(), dto.getId())) {
            throw new DuplicateResourceException("genero", "nombre", dto.getNombre());
        }

        Genero genero = generoRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("genero", "id", dto.getId()));

        GeneroMapper.copyToExistingEntity(dto, genero);
        // Al estar en una transacción (@Transactional), el save no es estrictamente
        // necesario para actualizar, pero se deja por claridad.
        return GeneroMapper.toDTO(generoRepository.save(genero));
    }

    @Override
    public void delete(Long id) {
        if (!generoRepository.existsById(id)) {
            throw new ResourceNotFoundException("genero", "id", id);
        }
        generoRepository.deleteById(id);
    }

    @Override
    public List<GenerosDTO> listAllPlain() {
        // Usamos el mapper para convertir la lista de entidades a DTOs
        List<Genero> generos = generoRepository.findAll(Sort.by("id"));
        return GeneroMapper.toDTOList(generos);
    }

    @Override
    public void ensureExists(String nombre) {
        if (!generoRepository.existsByNombre(nombre)) {
            GenerosDTO nuevo = new GenerosDTO();
            nuevo.setNombre(nombre);
            this.create(nuevo);
        }
    }

    // Método opcional si necesitas la entidad pura en otros servicios
    @Override
    public List<Genero> findAllGeneros() {
        return generoRepository.findAll();
    }
}