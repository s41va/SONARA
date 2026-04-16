package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.repositories.ArtistaRepository;
import com.dawm.sonara.response.ArtistaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
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
    public List<ArtistaDTO> obtenerTodosOrdenados(String campo, String direccion) {
        Sort sort = direccion.equalsIgnoreCase("asc")
                ? Sort.by(campo).ascending()
                : Sort.by(campo).descending();

        return artistaRepository.findAll(sort).stream()
                .map(ArtistaDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ArtistaDTO obtenerPorIdCompleto(Integer id) {
        // 1. Buscamos primero en nuestra DB para tener el nombre correcto
        Artista local = artistaRepository.findById(id).orElse(null);
        if (local == null) return null;

        // 2. Llamamos a la API usando el nombre que tenemos guardado
        String url = apiUrl + "/search.php?s=" + local.getNombre();
        ArtistaResponse response = restTemplate.getForObject(url, ArtistaResponse.class);

        if (response != null && !response.getArtistas().isEmpty()) {
            // 3. Convertimos la info de la API a DTO
            ArtistaDTO dto = new ArtistaDTO(response.getArtistas().get(0));
            // 4. Le inyectamos los votos que tenemos en nuestra DB local
            dto.setVotosRanking(local.getVotosRanking());
            return dto;
        }

        // Si la API no lo encuentra, devolvemos al menos lo que tenemos local
        return new ArtistaDTO(local);
    }

    @Override
    public void eliminar(Integer id) {
        // 1. Verificamos si existe antes de intentar borrar
        if (artistaRepository.existsById(id)) {
            // 2. Borramos de la base de datos local
            artistaRepository.deleteById(id);
        } else {
            // Opcional: Podrías lanzar una excepción personalizada aquí
            // throw new EntityNotFoundException("El artista con ID " + id + " no existe.");
            System.out.println("Intento de borrar artista inexistente: " + id);
        }
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