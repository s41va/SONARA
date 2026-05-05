package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artista.ArtistaRankingDTO;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.services.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "Ranking y Votos", description = "Controlador para gestionar los votos y visualizar rankings locales y globales")
public class RankingController {

    @Autowired
    private VotoService votoService;

    // --- SECCIÓN DE CONSULTAS (RANKING) ---

    @Operation(summary = "Ranking por Localidad", description = "Obtiene el Top de artistas en una ciudad específica")
    @GetMapping("/localidad/{nombreCiudad}")
    public ResponseEntity<List<ArtistaRankingDTO>> getRankingByLocalidad(@PathVariable String nombreCiudad) {
        return ResponseEntity.ok(votoService.getRankingLocal(nombreCiudad));
    }

    @Operation(summary = "Ranking Global", description = "Obtiene los artistas más votados en toda la plataforma")
    @GetMapping("/global")
    public ResponseEntity<List<ArtistaRankingDTO>> getRankingGlobal() {
        return ResponseEntity.ok(votoService.getRankingGlobal());
    }

    // --- SECCIÓN DE ACCIONES (VOTOS) ---

    @Operation(summary = "Votar Artista", description = "Registra un voto único de un usuario para un artista")
    @PostMapping("/votar/{artistaId}")
    public ResponseEntity<?> votar(@PathVariable String artistaId) {
        // NOTA: Cuando implementes JWT, obtendrás el usuario del SecurityContext.
        // Por ahora, necesitamos una forma de saber quién vota para probarlo.
        // Simulamos un usuario para la lógica (necesitarás inyectar tu UserService aquí)

        try {
            // Usuario usuarioLogueado = userService.getObjetoUsuarioLogueado();
            // votoService.votar(artistaId, usuarioLogueado);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            // Devuelve el mensaje "Ya has votado a este artista" o "Artista no encontrado"
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}