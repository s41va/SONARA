package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaRankingDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.entities.Voto;
import com.dawm.sonara.mappers.RankingMapper;
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

    @Autowired
    private RankingMapper rankingMapper;

    @Override
    @Transactional
    public void votar(String artistaId, Usuario usuario) {
        // 1. Evitar que el mismo usuario vote dos veces al mismo artista
        if (votoRepository.existsByUsuarioIdAndArtistaId(usuario.getId(), artistaId)) {
            throw new RuntimeException("Ya has votado a este artista anteriormente.");
        }

        // 2. Buscar el artista (Para asegurar que existe antes de votar)
        Artista artista = artistaRepository.findById(artistaId)
                .orElseThrow(() -> new RuntimeException("Artista no encontrado con ID: " + artistaId));

        // 3. Crear el registro del voto
        Voto voto = new Voto();
        voto.setArtista(artista);
        voto.setUsuario(usuario);

        // Seguridad: Si el usuario no tiene localidad, evitamos que pete el sistema
        if (usuario.getLocalidad() != null) {
            voto.setLocalidad(usuario.getLocalidad().getNombreCiudad());
        } else {
            // Valor por defecto para no romper los rankings si el perfil está incompleto
            voto.setLocalidad("Desconocida");
        }

        votoRepository.save(voto);

        // 4. Actualizar el contador en la tabla Artista (Caché para el Ranking Global)
        artista.setVotosRanking(artista.getVotosRanking() + 1);
        artistaRepository.save(artista);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtistaRankingDTO> getRankingLocal(String ciudad) {
        // Obtenemos los Object[] crudos del repositorio
        List<Object[]> resultados = votoRepository.findRankingByLocalidad(ciudad);
        // El mapper se encarga de convertirlos a DTOs limpios
        return rankingMapper.toDTOList(resultados);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArtistaRankingDTO> getRankingGlobal() {
        // Obtenemos los Object[] crudos del repositorio
        List<Object[]> resultados = votoRepository.findRankingGlobal();
        // El mapper se encarga de convertirlos a DTOs limpios
        return rankingMapper.toDTOList(resultados);
    }
}