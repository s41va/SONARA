package com.dawm.sonara.servicies;

import com.dawm.sonara.dtos.artistas.ArtistasCreateDTO;
import com.dawm.sonara.dtos.artistas.ArtistasDetailDTO;
import com.dawm.sonara.dtos.artistas.ArtistasUpdateDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.exceptions.DuplicateResourceException;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.ArtistasMapper;
import com.dawm.sonara.repositories.ArtistasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class ArtistaServiceImpl implements ArtistaService{

    @Autowired
    private ArtistasRepository artistasRepository;



    @Override
    public void create(ArtistasCreateDTO dto) {
        if (artistasRepository.existsById(dto.getArtista_id())){
            throw new DuplicateResourceException("artista", "nombre", dto.getNombre_artistico());
        }
        Artista artista = ArtistasMapper.toEntity(dto);
        artistasRepository.save(artista);

    }

    @Override
    public void update(ArtistasUpdateDTO dto) {
        if (artistasRepository.existsById(dto.getArtista_id())){
            throw new DuplicateResourceException("artista", "nombre", dto.getNombre_artistico());
        }

        Artista artista = artistasRepository.findById(dto.getArtista_id())
                .orElseThrow(()-> new ResourceNotFoundException("artista", "nombre", dto.getArtista_id()));

        ArtistasMapper.copyToExistingEntity(dto, artista);
        artistasRepository.save(artista);

    }

    @Override
    public void delete(Long id) {
       if (!artistasRepository.existsById(id)){
           throw new ResourceNotFoundException("artista", "nombre", id);
       }
       artistasRepository.delete(id);
    }

    @Override
    public ArtistasUpdateDTO getForEdit(Long id) {
        Artista artista = artistasRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("artista", "id", id));
        return ArtistasMapper.toUpdateDTO(artista);
    }

    @Override
    public ArtistasDetailDTO getDetail(Long id) {
        Artista artista = artistasRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("artista", "id", id));
        return ArtistasMapper.toDetailDTO(artista);
    }
}
