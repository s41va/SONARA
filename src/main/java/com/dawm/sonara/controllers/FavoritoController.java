package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.dtos.artista.ArtistaFavoritoDTO;
import com.dawm.sonara.services.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @Operation(summary = "Añadir artista a favoritos por ID")
    @PostMapping("/artistas/{usuarioId}/{artistaId}")
    public ResponseEntity<Void> agregarFavorito(
            @PathVariable Long usuarioId,
            @PathVariable String artistaId) {

        favoritoService.agregarArtistaAFavoritos(usuarioId, artistaId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Quitar de favoritos")
    @DeleteMapping("/artistas/{usuarioId}/{artistaId}")
    public ResponseEntity<Void> eliminarFavorito(@PathVariable Long usuarioId, @PathVariable String artistaId) {
        favoritoService.eliminarArtistaDeFavoritos(usuarioId, artistaId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar mis favoritos")
    @GetMapping("/artistas/{usuarioId}")
    public ResponseEntity<Set<ArtistaFavoritoDTO>> listarFavoritos(@PathVariable Long usuarioId) {
        // Devuelve el SET de DTOs limpios (id, nombre, foto, genero)
        return ResponseEntity.ok(favoritoService.obtenerMisArtistasFavoritos(usuarioId));
    }
}