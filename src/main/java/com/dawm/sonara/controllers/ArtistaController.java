package com.dawm.sonara.controllers;

import com.dawm.sonara.entities.Artista;
import com.dawm.sonara.services.ArtistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/artistas")
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService; // Inyectamos la Interfaz, no la clase Impl

    @GetMapping("/{nombre}")
    public ResponseEntity<Artista> obtener(@PathVariable String nombre) {
        return ResponseEntity.ok(artistaService.buscarPorNombre(nombre));
    }
}