package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
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

    @Transactional
    @Override
    public void agregarArtistaAFavoritos(Long usuarioId, ArtistaDTO artistaDTO) {
        // 1. Buscar al usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        // 2. Asegurarnos de que el artista existe en nuestra DB local.
        // Si no existe, guardarArtistaLocal lo importa de la API y lo guarda.
        ArtistaDTO guardadoDTO = artistaService.guardarArtistaLocal(artistaDTO);

        // 3. Obtener la entidad Artista de nuestra DB
        Artista artista = artistaRepository.findById(guardadoDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Artista", "id", guardadoDTO.getId()));

        // 4. Añadir a la colección (Set evita duplicados automáticamente)
        usuario.getArtistasFavoritos().add(artista);

        // 5. Persistir (Spring se encarga de insertar en la tabla intermedia)
        usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public void eliminarArtistaDeFavoritos(Long usuarioId, String artistaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        // Simplemente lo quitamos del Set. JPA se encarga de borrar la fila en la tabla intermedia.
        usuario.getArtistasFavoritos().removeIf(a -> a.getId().equals(artistaId));

        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<ArtistaDTO> obtenerMisArtistasFavoritos(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuarioId));

        // Convertimos las entidades Artista a DTO para el Frontend
        return usuario.getArtistasFavoritos().stream()
                .map(ArtistaDTO::new)
                .collect(Collectors.toSet());
    }
}