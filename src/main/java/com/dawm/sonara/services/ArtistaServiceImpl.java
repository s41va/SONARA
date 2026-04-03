package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.response.ArtistaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ArtistaServiceImpl implements ArtistaService {

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
}