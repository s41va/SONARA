package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.services.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artistas")
@Tag(name = "Artistas", description = "Gestión del catálogo de artistas e importación desde TheAudioDB")
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    @Operation(summary = "Obtener detalle por nombre (API Externa)")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ArtistaDTO> obtenerPorNombre(@PathVariable String nombre) {
        ArtistaDTO dto = artistaService.buscarPorNombre(nombre);
        return (dto != null) ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Listar artistas locales", description = "Lista artistas guardados en la DB local")
    @GetMapping
    public ResponseEntity<List<ArtistaDTO>> listar(
            @RequestParam(defaultValue = "nombre") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(artistaService.obtenerTodosOrdenados(sortField, sortDir));
    }

    @Operation(summary = "Obtener detalle completo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ArtistaDTO> obtenerDetalle(@PathVariable String id) {
        ArtistaDTO detalle = artistaService.obtenerPorIdCompleto(id);
        return (detalle != null) ? ResponseEntity.ok(detalle) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Importar artista", description = "Guarda un artista de la API en la base de datos local")
    @PostMapping
    public ResponseEntity<ArtistaDTO> crear(@RequestBody ArtistaDTO artistaDTO) {
        ArtistaDTO guardado = artistaService.guardarArtistaLocal(artistaDTO);
        return ResponseEntity.status(201).body(guardado);
    }

    @Operation(summary = "Eliminar artista local")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        artistaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}