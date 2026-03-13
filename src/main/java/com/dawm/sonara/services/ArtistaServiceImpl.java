package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artistas.ArtistasCreateDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDetailDTO;
import com.dawm.sonara.dtos.artistas.ArtistasUpdateDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.entities.Genero;
import com.dawm.sonara.exceptions.DuplicateResourceException;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.ArtistasMapper;
import com.dawm.sonara.repositories.ArtistasRepository;
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
public class ArtistaServiceImpl implements ArtistaService{

    @Autowired
    private ArtistasRepository artistasRepository;

    @Autowired
    private GeneroRepository generoRepository;


    @Override
    public Page<ArtistasDTO> list(Pageable pageable) {
        return artistasRepository.findAll(pageable).map(ArtistasMapper::toDTO);
    }

    @Override
    public ArtistasUpdateDTO getForEdit(Long id) {
        Artista artista = artistasRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user", "id", id));
        return ArtistasMapper.toUpdateDTO(artista);
    }

    @Override
    public ArtistasDTO create(ArtistasCreateDTO dto) {
        if (artistasRepository.existsByNombre(dto.getNombre_artistico())){
            throw new DuplicateResourceException("artista", "name", dto.getNombre_artistico());
        }
        Artista artista = ArtistasMapper.toEntity(dto);
        artista = artistasRepository.save(artista);
        return ArtistasMapper.toDTO(artista);
    }

    @Override
    public ArtistasDTO update(ArtistasUpdateDTO dto) {
        if (artistasRepository.existsByNombreAndIdNot(dto.getNombre_artistico(), dto.getArtista_id())){
            throw new DuplicateResourceException("artista", "name", dto.getNombre_artistico());
        }
        Artista artista = artistasRepository.findById(dto.getArtista_id())
                .orElseThrow(()-> new ResourceNotFoundException("user", "id", dto.getArtista_id()));


        ArtistasMapper.copyToExistingEntity(dto,artista);
        artista = artistasRepository.save(artista);
        return ArtistasMapper.toDTO(artista);
    }

    @Override
    public void delete(Long id) {
        if (!artistasRepository.existsById(id)){
            throw new ResourceNotFoundException("artista", "id", id);
        }
        artistasRepository.deleteById(id);
    }

    @Override
    public ArtistasDetailDTO getDetail(Long id) {
        Artista artista = artistasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("artista", "id", id));
        return ArtistasMapper.toDetailDTO(artista);
    }

    @Override
    public List<Genero> findAllGeneros() {
        return generoRepository.findAll();
    }

    @Override
    public List<ArtistasDTO> listAll(Sort name) {
        return artistasRepository.findAll(name)
                .stream()
                .map(ArtistasMapper::toDTO)
                .toList();
    }
}
