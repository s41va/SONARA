package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artistasOLD.ArtistasCreateDTO;
import com.dawm.sonara.dtos.artistasOLD.ArtistasDTO;
import com.dawm.sonara.dtos.artistasOLD.ArtistasDetailDTO;
import com.dawm.sonara.dtos.artistasOLD.ArtistasUpdateDTO;
import com.dawm.sonara.entities.ArtistaOLD;
import com.dawm.sonara.entities.Genero;
import com.dawm.sonara.exceptions.DuplicateResourceException;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.ArtistasMapperOLD;
import com.dawm.sonara.repositories.ArtistasRepositoryOLD;
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
public class ArtistaServiceOLDImpl implements ArtistaServiceOLD {

    @Autowired
    private ArtistasRepositoryOLD artistasRepositoryOLD;

    @Autowired
    private GeneroRepository generoRepository;


    @Override
    public Page<ArtistasDTO> list(Pageable pageable) {
        return artistasRepositoryOLD.findAll(pageable).map(ArtistasMapperOLD::toDTO);
    }

    @Override
    public ArtistasUpdateDTO getForEdit(Long id) {
        ArtistaOLD artistaOLD = artistasRepositoryOLD.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("user", "id", id));
        return ArtistasMapperOLD.toUpdateDTO(artistaOLD);
    }

    @Override
    public ArtistasDTO create(ArtistasCreateDTO dto) {
        if (artistasRepositoryOLD.existsByNombre(dto.getNombre_artistico())){
            throw new DuplicateResourceException("artista", "name", dto.getNombre_artistico());
        }
        ArtistaOLD artistaOLD = ArtistasMapperOLD.toEntity(dto);
        artistaOLD = artistasRepositoryOLD.save(artistaOLD);
        return ArtistasMapperOLD.toDTO(artistaOLD);
    }

    @Override
    public ArtistasDTO update(ArtistasUpdateDTO dto) {
        if (artistasRepositoryOLD.existsByNombreAndIdNot(dto.getNombre(), dto.getId())){
            throw new DuplicateResourceException("artista", "name", dto.getNombre());
        }
        ArtistaOLD artistaOLD = artistasRepositoryOLD.findById(dto.getId())
                .orElseThrow(()-> new ResourceNotFoundException("user", "id", dto.getId()));


        ArtistasMapperOLD.copyToExistingEntity(dto, artistaOLD);
        artistaOLD = artistasRepositoryOLD.save(artistaOLD);
        return ArtistasMapperOLD.toDTO(artistaOLD);
    }

    @Override
    public void delete(Long id) {
        if (!artistasRepositoryOLD.existsById(id)){
            throw new ResourceNotFoundException("artista", "id", id);
        }
        artistasRepositoryOLD.deleteById(id);
    }

    @Override
    public ArtistasDetailDTO getDetail(Long id) {
        ArtistaOLD artistaOLD = artistasRepositoryOLD.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("artista", "id", id));
        return ArtistasMapperOLD.toDetailDTO(artistaOLD);
    }

    @Override
    public List<Genero> findAllGeneros() {
        return generoRepository.findAll();
    }

    @Override
    public List<ArtistasDTO> listAll(Sort name) {
        return artistasRepositoryOLD.findAll(name)
                .stream()
                .map(ArtistasMapperOLD::toDTO)
                .toList();
    }
}
