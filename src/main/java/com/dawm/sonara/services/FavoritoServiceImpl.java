package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.dtos.artista.ArtistaFavoritoDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.mappers.ArtistaFavoritoMapper;
import com.dawm.sonara.repositories.ArtistaRepository;
import com.dawm.sonara.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FavoritoServiceImpl implements FavoritoService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ArtistaRepository artistaRepository;
    @Autowired
    private ArtistaService artistaService;
    @Autowired
    private ArtistaFavoritoMapper favoritoMapper;

    @Transactional
    @Override
    public void agregarArtistaAFavoritos(Long usuarioId, String artistaId) {
        // 1. Buscar al usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        // 2. Buscar artista en DB local o importar de API externa
        Artista artista = artistaRepository.findById(artistaId)
                .orElseGet(() -> {
                    // Si no existe localmente, lo buscamos en la API externa por su ID
                    ArtistaDTO dtoExterno = artistaService.buscarPorIdExterno(artistaId);

                    if (dtoExterno == null) {
                        throw new ResourceNotFoundException("Artista Externo", "id", artistaId);
                    }

                    // Lo guardamos localmente usando tu método "Puente"
                    ArtistaDTO guardado = artistaService.guardarArtistaLocal(dtoExterno);

                    return artistaRepository.findById(guardado.getId()).get();
                });

        // 3. Añadir a favoritos
        usuario.getArtistasFavoritos().add(artista);
        usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public void eliminarArtistaDeFavoritos(Long usuarioId, String artistaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        usuario.getArtistasFavoritos().removeIf(a -> a.getId().equals(artistaId));
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<ArtistaFavoritoDTO> obtenerMisArtistasFavoritos(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        // Aquí usamos el mapper para limpiar el JSON
        return usuario.getArtistasFavoritos().stream()
                .map(favoritoMapper::toDTO)
                .collect(Collectors.toSet());
    }
}