package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaRankingDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.entities.Voto;
import com.dawm.sonara.repositories.ArtistaRepository;
import com.dawm.sonara.repositories.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VotoServiceImpl implements VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Override
    @Transactional
    public void votar(String artistaId, Usuario usuario) {
        // 1. Evitar que el mismo usuario vote dos veces al mismo artista
        if (votoRepository.existsByUsuarioIdAndArtistaId(usuario.getId(), artistaId)) {
            throw new RuntimeException("Ya has votado a este artista anteriormente.");
        }

        // 2. Buscar el artista
        Artista artista = artistaRepository.findById(artistaId)
                .orElseThrow(() -> new RuntimeException("Artista no encontrado con ID: " + artistaId));

        // 3. Crear y guardar el voto con la localidad del usuario
        Voto voto = new Voto();
        voto.setArtista(artista);
        voto.setUsuario(usuario);
        // Usamos la localidad que el usuario tiene asignada en su perfil
        voto.setLocalidad(usuario.getLocalidad().getNombreCiudad());
        votoRepository.save(voto);

        // 4. Actualizar la caché de votos en la entidad Artista para consultas rápidas
        artista.setVotosRanking(artista.getVotosRanking() + 1);
        artistaRepository.save(artista);
    }

    @Override
    public List<ArtistaRankingDTO> getRankingLocal(String ciudad) {
        List<Object[]> resultados = votoRepository.findRankingByLocalidad(ciudad);
        return convertirARankingDTO(resultados);
    }

    @Override
    public List<ArtistaRankingDTO> getRankingGlobal() {
        List<Object[]> resultados = votoRepository.findRankingGlobal();
        return convertirARankingDTO(resultados);
    }

    /**
     * Convierte la respuesta cruda de la base de datos (Object[]) al DTO que entiende Angular.
     * Estructura del array: [0:id, 1:nombre, 2:foto, 3:totalVotos]
     */
    private List<ArtistaRankingDTO> convertirARankingDTO(List<Object[]> resultados) {
        return resultados.stream()
                .map(res -> new ArtistaRankingDTO(
                        (String) res[0],
                        (String) res[1],
                        (String) res[2],
                        (Long) res[3]
                ))
                .toList();
    }
}