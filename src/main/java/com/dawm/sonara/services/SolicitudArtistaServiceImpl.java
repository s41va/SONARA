package com.dawm.sonara.services;

import com.dawm.sonara.dtos.solicitudArtista.*;
import com.dawm.sonara.entities.*;
import com.dawm.sonara.entities.enums.Estado;
import com.dawm.sonara.repositories.*;
import com.dawm.sonara.mappers.SolicitudArtistaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudArtistaServiceImpl implements SolicitudArtistaService {

    @Autowired
    private SolicitudArtistaRepository solicitudRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GeneroRepository generoRepository;


    @Override
    @Transactional
    public void crearSolicitud(SolicitudArtistaCreateDTO dto, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        SolicitudArtista s = new SolicitudArtista();
        s.setNombreArtista(dto.getNombreArtista());
        s.setGeneroSugerido(dto.getGeneroSugerido());
        s.setDescripcion(dto.getDescripcion());
        s.setFotoUrl(dto.getFotoUrl());
        s.setUsuarioSolicitante(usuario);
        solicitudRepository.save(s);
    }

    // 2. Método que faltaba implementar
    @Override
    @Transactional(readOnly = true)
    public List<SolicitudArtistaDTO> obtenerPendientes() {
        return solicitudRepository.findByEstado(Estado.PENDIENTE)
                .stream()
                .map(SolicitudArtistaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void actualizarYAprobar(Long id, SolicitudArtistaUpdateDTO dto) {
        SolicitudArtista s = solicitudRepository.findById(id).orElseThrow();

        // 1. Solo creamos el artista si NO existe ya uno con ese nombre
        if (!artistaRepository.existsByNombre(dto.getNombreArtista())) {
            Artista artistaOficial = new Artista();
            artistaOficial.setId("LOC_" + UUID.randomUUID().toString().substring(0, 8));
            artistaOficial.setNombre(dto.getNombreArtista());
            artistaOficial.setGenero(dto.getGeneroSugerido());
            artistaOficial.setFoto(dto.getFotoUrl());
            artistaOficial.setVotosRanking(0);

            artistaRepository.save(artistaOficial);
        }

        // 2. Marcamos como aprobada (independientemente de si ya existía el artista)
        s.setEstado(Estado.APROBADA);
        solicitudRepository.save(s);
    }

    @Override
    @Transactional
    public void rechazarSolicitud(Long id) {
        SolicitudArtista s = solicitudRepository.findById(id).orElseThrow();

        if (s.getEstado() == Estado.APROBADA) {
            artistaRepository.findByNombre(s.getNombreArtista())
                    .ifPresent(artistaRepository::delete);
        }

        s.setEstado(Estado.RECHAZADA);
        solicitudRepository.save(s);
    }
}