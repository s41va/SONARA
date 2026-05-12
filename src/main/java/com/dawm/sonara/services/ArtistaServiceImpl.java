package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.repositories.ArtistaRepository;
import com.dawm.sonara.response.ArtistaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistaServiceImpl implements ArtistaService {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${theaudiodb.api.url}")
    private String apiUrl;

    // --- MÉTODOS DE BÚSQUEDA EXTERNA ---

    @Override
    public ArtistaDTO buscarPorNombre(String nombre) {
        String url = apiUrl + "/search.php?s=" + nombre;
        try {
            ArtistaResponse response = restTemplate.getForObject(url, ArtistaResponse.class);
            if (response != null && response.getArtistas() != null && !response.getArtistas().isEmpty()) {
                return new ArtistaDTO(response.getArtistas().get(0));
            }
        } catch (Exception e) {
            System.err.println("Error buscando por nombre en AudioDB: " + e.getMessage());
        }
        return null;
    }

    @Override
    public ArtistaDTO buscarPorIdExterno(String id) {
        String url = apiUrl + "/artist.php?i=" + id;
        try {
            ArtistaResponse response = restTemplate.getForObject(url, ArtistaResponse.class);
            if (response != null && response.getArtistas() != null && !response.getArtistas().isEmpty()) {
                return new ArtistaDTO(response.getArtistas().get(0));
            }
        } catch (Exception e) {
            System.err.println("Error buscando por ID en AudioDB: " + e.getMessage());
        }
        return null;
    }

    // --- MÉTODOS DE BASE DE DATOS LOCAL ---

    @Override
    public List<ArtistaDTO> obtenerTodosOrdenados(String campo, String direccion) {
        Sort sort = direccion.equalsIgnoreCase("asc") ? Sort.by(campo).ascending() : Sort.by(campo).descending();
        return artistaRepository.findAll(sort).stream()
                .map(ArtistaDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ArtistaDTO obtenerPorIdCompleto(String id) {
        Artista local = artistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("artista", "id", id));

        ArtistaDTO dtoApi = buscarPorNombre(local.getNombre());
        if (dtoApi != null) {
            dtoApi.setVotosRanking(local.getVotosRanking());
            return dtoApi;
        }
        return new ArtistaDTO(local);
    }

    @Override
    @Transactional
    public void eliminar(String id) {
        if (!artistaRepository.existsById(id)) {
            throw new ResourceNotFoundException("artista", "id", id);
        }
        artistaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ArtistaDTO guardarArtistaLocal(ArtistaDTO dto) {
        // AQUÍ BUSCA PRIMERO: Si ya está en la DB local, no hace nada más
        return artistaRepository.findById(dto.getId())
                .map(ArtistaDTO::new)
                .orElseGet(() -> {
                    // Solo si NO está, lo crea de cero
                    Artista nuevo = new Artista();
                    nuevo.setId(dto.getId());
                    nuevo.setNombre(dto.getNombre());
                    nuevo.setGenero(dto.getGenero());
                    nuevo.setFoto(dto.getFoto());
                    nuevo.setVotosRanking(0);
                    nuevo.setUltimaSincronizacion(LocalDateTime.now());

                    if (dto.getGenero() != null) {
                        generoService.ensureExists(dto.getGenero());
                    }

                    Artista guardado = artistaRepository.save(nuevo);
                    return new ArtistaDTO(guardado);
                });
    }
}