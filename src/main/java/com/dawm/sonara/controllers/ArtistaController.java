package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.repositories.ArtistaRepository;
import com.dawm.sonara.services.ArtistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artistas")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")// Permite que Angular conecte sin problemas de CORS
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    @Autowired
    private ArtistaRepository artistaRepository;

    /**
     * Obtiene el detalle de un artista desde la API externa (TheAudioDB).
     * Se usa para la página de "Perfil de Artista".
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ArtistaDTO> obtener(@PathVariable String nombre) {
        ArtistaDTO dto = artistaService.buscarPorNombre(nombre);

        return (dto != null)
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ArtistaDTO>> listar(
            @RequestParam(defaultValue = "nombre") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(artistaService.obtenerTodosOrdenados(sortField, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistaDTO> obtenerDetalle(@PathVariable Integer id) {
        // Usamos el ID para buscar en la API (o el nombre si prefieres)
        ArtistaDTO detalle = artistaService.obtenerPorIdCompleto(id);

        return (detalle != null)
                ? ResponseEntity.ok(detalle)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (artistaRepository.existsById(id)) { // Necesitarías inyectar el repo o que el service devuelva boolean
            artistaService.eliminar(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    /**
     * Obtiene el Top 10 de artistas con más votos en nuestra base de datos local.
     * Devuelve una lista de DTOs simplificados.
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<ArtistaDTO>> obtenerRanking() {
        List<ArtistaDTO> ranking = artistaService.obtenerRanking();

        if (ranking.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ranking);
    }

    /**
     * Registra un voto para un artista.
     * Si el artista no existe en nuestra DB local (pero sí en la API),
     * el servicio lo creará automáticamente (Lazy Insert).
     * * @param id El ID proveniente de la API (idArtist)
     * @param nombre El nombre del artista para guardarlo en la DB local
     */
    @PostMapping("/votar/{id}")
    public ResponseEntity<Void> votar(@PathVariable String id, @RequestParam String nombre) {
        try {
            // Convertimos el ID de String a Integer para nuestra DB
            Integer idNumerico = Integer.parseInt(id);
            artistaService.votarArtista(idNumerico, nombre);
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}