package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.artista.ArtistaDTO;
import com.dawm.sonara.services.ArtistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/artistas")
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    @GetMapping("/{nombre}")
    public ResponseEntity<ArtistaDTO> obtener(@PathVariable String nombre) {
        // Llamamos al servicio que ya devuelve el DTO limpio
        ArtistaDTO dto = artistaService.buscarPorNombre(nombre);

        return (dto != null)
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }
}