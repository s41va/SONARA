package com.dawm.sonara.servicies;

import com.dawm.sonara.dtos.generos.GenerosCreateDTO;
import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.dtos.generos.GenerosDetailDTO;
import com.dawm.sonara.dtos.generos.GenerosUpdateDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.entities.Genero;
import com.dawm.sonara.exceptions.DuplicateResourceException;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.ArtistasMapper;
import com.dawm.sonara.mappers.GeneroMapper;
import com.dawm.sonara.repositories.GeneroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class GeneroServiceImpl implements GeneroService{

    @Autowired
    private GeneroRepository generoRepository;

    @Override
    public Page<GenerosDTO> list(Pageable pageable) {
        return generoRepository.findAll(pageable).map(GeneroMapper::toDTO);
    }

    @Override
    public GenerosDTO create(GenerosCreateDTO dto) {
        if (generoRepository.existsById(dto.getId())){
            throw new DuplicateResourceException("artista", "nombre", dto.getId());
        }
        Genero genero= GeneroMapper.toEntity(dto);
        genero = generoRepository.save(genero);
        return GeneroMapper.toDTO(genero);
    }

    @Override
    public GenerosDTO update(GenerosUpdateDTO dto) {
        if (generoRepository.existsById(dto.getId())){
            throw new DuplicateResourceException("genero", "id", dto.getId());
        }

        Genero genero = generoRepository.findById(dto.getId())
                .orElseThrow(()-> new ResourceNotFoundException("genero", "id", dto.getId()));

        genero = generoRepository.save(genero);

        return GeneroMapper.toDTO(genero);
    }

    @Override
    public void delete(Long id) {
        if (!generoRepository.existsById(id)){
            throw new ResourceNotFoundException("genero", "id", id);
        }
        generoRepository.delete(id);
    }

    @Override
    public GenerosDetailDTO getDetail(Long id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("genero", "id", id));

        return GeneroMapper.toDetailDTO(genero);
    }

    @Override
    public GenerosUpdateDTO getForEdit(Long id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("genero", "id", id));
        return GeneroMapper.toUpdateDTO(genero);
    }

    @Override
    public GenerosUpdateDTO getGeneroById(Long id) {
        Genero genero = generoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("genero", "id", id));
        return GeneroMapper.toUpdateDTO(genero);
    }
}
