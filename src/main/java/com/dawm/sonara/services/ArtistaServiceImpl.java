package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.repositories.ArtistaRepository;
import com.dawm.sonara.response.ArtistaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistaServiceImpl implements ArtistaService {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Value("${theaudiodb.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ArtistaDTO buscarPorNombre(String nombre) {
        String url = apiUrl + "/search.php?s=" + nombre;

        // 1. Recibimos el Response (el envoltorio)
        ArtistaResponse response = restTemplate.getForObject(url, ArtistaResponse.class);

        // 2. Si hay datos, extraemos el primero y lo convertimos a DTO
        if (response != null && response.getArtistas() != null && !response.getArtistas().isEmpty()) {
            // Usamos el constructor que creamos en el DTO
            return new ArtistaDTO(response.getArtistas().get(0));
        }
        return null;
    }

    @Override
    public List<ArtistaDTO> obtenerRanking() {
        // 1. Obtenemos las entidades de la DB
        List<Artista> entidades = artistaRepository.findTop10ByOrderByVotosRankingDesc();

        // 2. Convertimos a DTOs usando Stream
        return entidades.stream()
                .map(ArtistaDTO::new) // Usa el nuevo constructor ArtistaDTO(Artista entidad)
                .collect(Collectors.toList());
    }

    @Override
    public void votarArtista(Integer id, String nombre) {
        // 1. Buscamos si ya existe en nuestra DB
        Artista artista = artistaRepository.findById(id).orElse(null);

        if (artista == null) {
            // 2. Si no existe, lo creamos (Lazy Insert)
            artista = new Artista();
            artista.setId(id);
            artista.setNombre(nombre);
            artista.setVotosRanking(1);
        } else {
            // 3. Si existe, sumamos voto
            artista.setVotosRanking(artista.getVotosRanking() + 1);
        }

        artistaRepository.save(artista);
    }
}