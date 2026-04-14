package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.services.ArtistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artistas")
@CrossOrigin(origins = "*") // Permite que Angular conecte sin problemas de CORS
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    /**
     * Obtiene el detalle de un artista desde la API externa (TheAudioDB).
     * Se usa para la página de "Perfil de Artista".
     */
    @GetMapping("/{nombre}")
    public ResponseEntity<ArtistaDTO> obtener(@PathVariable String nombre) {
        ArtistaDTO dto = artistaService.buscarPorNombre(nombre);

        return (dto != null)
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
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