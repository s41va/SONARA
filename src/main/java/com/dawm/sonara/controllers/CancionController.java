package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.cancion.CancionDTO;
import com.dawm.sonara.entities.Cancion;
import com.dawm.sonara.services.CancionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/canciones")
// @CrossOrigin(origins = "http://localhost:4200") // El puerto de tu Angular
public class CancionController {

    @Autowired
    private CancionService cancionService;

    @GetMapping("/buscar")
    public ResponseEntity<CancionDTO> buscar(
            @RequestParam String artista,
            @RequestParam String titulo) {

        CancionDTO cancion = cancionService.buscarCancion(artista, titulo);

        if (cancion == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cancion);
    }
}