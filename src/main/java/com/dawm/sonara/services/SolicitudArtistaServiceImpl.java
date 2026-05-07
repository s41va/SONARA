package com.dawm.sonara.services;

import com.dawm.sonara.dtos.solicitudArtista.*;
import com.dawm.sonara.entities.*;
import com.dawm.sonara.entities.enums.EstadoSolicitud;
import com.dawm.sonara.repositories.*;
import com.dawm.sonara.services.SolicitudArtistaService;
import com.dawm.sonara.mappers.SolicitudArtistaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudArtistaServiceImpl implements SolicitudArtistaService {

    @Autowired private SolicitudArtistaRepository solicitudRepository;
    @Autowired private ArtistaRepository artistaRepository;
    @Autowired private UsuarioRepository usuarioRepository;

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
        return solicitudRepository.findByEstado(EstadoSolicitud.PENDIENTE)
                .stream()
                .map(SolicitudArtistaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void actualizarYAprobar(Long id, SolicitudArtistaUpdateDTO dto) {
        SolicitudArtista s = solicitudRepository.findById(id).orElseThrow();

        // 1. Creamos el Artista real (Solo con los campos que sí guardamos)
        Artista artistaOficial = new Artista();
        artistaOficial.setId("LOC_" + UUID.randomUUID().toString().substring(0, 8));
        artistaOficial.setNombre(dto.getNombreArtista());
        artistaOficial.setGenero(dto.getGeneroSugerido());
        artistaOficial.setFoto(dto.getFotoUrl());
        artistaOficial.setVotosRanking(0);

        artistaRepository.save(artistaOficial);

        // 2. Cerramos la solicitud
        s.setEstado(EstadoSolicitud.APROBADA);
        solicitudRepository.save(s);
    }

    @Override
    @Transactional // 3. Añadido Transactional
    public void rechazarSolicitud(Long id) {
        SolicitudArtista s = solicitudRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        s.setEstado(EstadoSolicitud.RECHAZADA);
        solicitudRepository.save(s);
    }
}