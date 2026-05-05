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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistaServiceImpl implements ArtistaService {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private GeneroService generoService;

    @Value("${theaudiodb.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ArtistaDTO buscarPorNombre(String nombre) {
        String url = apiUrl + "/search.php?s=" + nombre;
        ArtistaResponse response = restTemplate.getForObject(url, ArtistaResponse.class);

        if (response != null && response.getArtistas() != null && !response.getArtistas().isEmpty()) {
            return new ArtistaDTO(response.getArtistas().get(0));
        }
        return null;
    }

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

        // Intentamos enriquecer con la API externa usando el nombre
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
        return artistaRepository.findById(dto.getId())
                .map(ArtistaDTO::new)
                .orElseGet(() -> {
                    Artista nuevo = new Artista();
                    nuevo.setId(dto.getId());
                    nuevo.setNombre(dto.getNombre());
                    nuevo.setBiografia(dto.getBiografia());
                    nuevo.setGenero(dto.getGenero());
                    nuevo.setFoto(dto.getFoto());
                    nuevo.setWeb(dto.getWeb());
                    nuevo.setVotosRanking(0);

                    // Aseguramos que el género exista en nuestra tabla de géneros
                    if (dto.getGenero() != null) {
                        generoService.ensureExists(dto.getGenero());
                    }

                    return new ArtistaDTO(artistaRepository.save(nuevo));
                });
    }
}