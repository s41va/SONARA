package com.dawm.sonara.services;

import com.dawm.sonara.dtos.cancion.CancionDTO;
import com.dawm.sonara.response.CancionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CancionServiceImpl implements CancionService {

    @Value("${theaudiodb.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public CancionDTO buscarCancion(String artista, String titulo) {
        // Ejemplo: searchtrack.php?s=coldplay&t=yellow
        String url = apiUrl + "/searchtrack.php?s=" + artista + "&t=" + titulo;

        CancionResponse response = restTemplate.getForObject(url, CancionResponse.class);

        if (response != null && response.getCanciones() != null && !response.getCanciones().isEmpty()) {
            return new CancionDTO(response.getCanciones().get(0));
        }
        return null;
    }
}