package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.solicitudArtista.SolicitudArtistaCreateDTO;
import com.dawm.sonara.dtos.solicitudArtista.SolicitudArtistaDTO;
import com.dawm.sonara.dtos.solicitudArtista.SolicitudArtistaUpdateDTO;
import com.dawm.sonara.services.SolicitudArtistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudArtistaController {

    @Autowired
    private SolicitudArtistaService solicitudService;

    // --- ENDPOINTS PARA USUARIOS ---

    @PostMapping
    public ResponseEntity<Void> crearSolicitud(@RequestBody SolicitudArtistaCreateDTO dto, Principal principal) {
        // Obtenemos el email del usuario logueado desde el token (Principal)
        solicitudService.crearSolicitud(dto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // --- ENDPOINTS PARA ADMINISTRADORES ---

    @GetMapping("/admin/pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SolicitudArtistaDTO>> listarPendientes() {
        return ResponseEntity.ok(solicitudService.obtenerPendientes());
    }

    @PutMapping("/admin/aprobar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> actualizarYAprobar(@PathVariable Long id, @RequestBody SolicitudArtistaUpdateDTO dto) {
        solicitudService.actualizarYAprobar(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/admin/rechazar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rechazarSolicitud(@PathVariable Long id) {
        solicitudService.rechazarSolicitud(id);
        return ResponseEntity.ok().build();
    }
}