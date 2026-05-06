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

    @Autowired // Inyectado como Bean (debes tener el @Bean en una Config class)
    private RestTemplate restTemplate;

    @Value("${theaudiodb.api.url}")
    private String apiUrl;

    /**
     * Busca un artista en la API externa.
     * Si lo encuentra, devuelve un DTO listo para ser usado o guardado.
     */
    @Override
    public ArtistaDTO buscarPorNombre(String nombre) {
        // Construimos la URL: ej. https://theaudiodb.com/api/v1/json/3/search.php?s=Eminem
        String url = apiUrl + "/search.php?s=" + nombre;

        try {
            ArtistaResponse response = restTemplate.getForObject(url, ArtistaResponse.class);

            if (response != null && response.getArtistas() != null && !response.getArtistas().isEmpty()) {
                // Devolvemos el primer resultado mapeado a DTO
                return new ArtistaDTO(response.getArtistas().get(0));
            }
        } catch (Exception e) {
            // Loguear el error si la API falla, pero no romper la app
            System.err.println("Error llamando a la API de AudioDB: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene los artistas de nuestra base de datos local.
     */
    @Override
    public List<ArtistaDTO> obtenerTodosOrdenados(String campo, String direccion) {
        Sort sort = direccion.equalsIgnoreCase("asc") ? Sort.by(campo).ascending() : Sort.by(campo).descending();
        return artistaRepository.findAll(sort).stream()
                .map(ArtistaDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Busca en local y añade información extra de la API (como biografía o web)
     * que no guardamos por ahorrar espacio.
     */
    @Override
    public ArtistaDTO obtenerPorIdCompleto(String id) {
        Artista local = artistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("artista", "id", id));

        // Enriquecemos con datos frescos de la API
        ArtistaDTO dtoApi = buscarPorNombre(local.getNombre());

        if (dtoApi != null) {
            // Respetamos los votos que ya tenemos en nuestra base de datos
            dtoApi.setVotosRanking(local.getVotosRanking());
            return dtoApi;
        }

        // Si la API falla, devolvemos lo que tenemos en local
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

    /**
     * El "Puente": Si el artista no existe en local, lo crea usando los datos del DTO
     * (incluyendo la foto que ahora sí persiste).
     */
    @Override
    @Transactional
    public ArtistaDTO guardarArtistaLocal(ArtistaDTO dto) {
        return artistaRepository.findById(dto.getId())
                .map(ArtistaDTO::new) // Si ya existe, lo devuelve
                .orElseGet(() -> {
                    Artista nuevo = new Artista();
                    nuevo.setId(dto.getId());
                    nuevo.setNombre(dto.getNombre());
                    nuevo.setGenero(dto.getGenero());

                    // persistimos la foto en la columna VARCHAR que añadimos
                    nuevo.setFoto(dto.getFoto());

                    // Estos se mantienen @Transient en la Entidad (no se guardan en DB)
                    nuevo.setBiografia(dto.getBiografia());
                    nuevo.setWeb(dto.getWeb());

                    nuevo.setVotosRanking(0);
                    nuevo.setUltimaSincronizacion(LocalDateTime.now());

                    // Mantenemos la integridad de géneros
                    if (dto.getGenero() != null) {
                        generoService.ensureExists(dto.getGenero());
                    }

                    Artista guardado = artistaRepository.save(nuevo);
                    return new ArtistaDTO(guardado);
                });
    }
}