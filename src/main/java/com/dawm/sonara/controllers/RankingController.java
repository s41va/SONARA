package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artista.ArtistaRankingDTO;
import com.dawm.sonara.entities.Usuario;
import com.dawm.sonara.repositories.UsuarioRepository;
import com.dawm.sonara.services.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
@Tag(name = "Ranking y Votos", description = "Controlador para gestionar los votos y visualizar rankings locales y globales")
public class RankingController {

    @Autowired
    private VotoService votoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

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
        try {
            // 1. Extraer el email del usuario autenticado (del JWT)
            String email = org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication().getName();

            // 2. Buscar al usuario en la DB
            Usuario usuarioLogueado = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 3. EJECUTAR la lógica
            votoService.votar(artistaId, usuarioLogueado);

            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}