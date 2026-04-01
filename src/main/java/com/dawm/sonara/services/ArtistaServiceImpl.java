package com.dawm.sonara.services;

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
    public Artista buscarPorNombre(String nombre) {
        String url = apiUrl + "/search.php?s=" + nombre;
        ArtistaResponse response = restTemplate.getForObject(url, ArtistaResponse.class);

        if (response != null && response.getArtistas() != null && !response.getArtistas().isEmpty()) {
            return new Artista(response.getArtistas().get(0));
        }
        return null;
    }
}