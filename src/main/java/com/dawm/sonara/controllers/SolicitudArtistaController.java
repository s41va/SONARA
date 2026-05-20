package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.solicitudArtista.SolicitudArtistaCreateDTO;
import com.dawm.sonara.dtos.solicitudArtista.SolicitudArtistaDTO;
import com.dawm.sonara.dtos.solicitudArtista.SolicitudArtistaUpdateDTO;
import com.dawm.sonara.services.SolicitudArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@Tag(name = "Solicitudes de Artista", description = "Controlador para la gestión del flujo de verificación y alta de nuevos artistas en la plataforma")
public class SolicitudArtistaController {

    @Autowired
    private SolicitudArtistaService solicitudService;

    // --- ENDPOINTS PARA USUARIOS ---

    @Operation(
            summary = "Crear una nueva solicitud de artista",
            description = "Permite a un usuario autenticado enviar una solicitud formal para registrarse o ser verificado como artista dentro de la plataforma."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud creada correctamente de manera exitosa"),
            @ApiResponse(responseCode = "400", description = "Datos de la solicitud inválidos o el usuario ya dispone de una solicitud en proceso"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Void> crearSolicitud(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para la creación de la solicitud de artista", required = true)
            @RequestBody SolicitudArtistaCreateDTO dto,
            @Parameter(hidden = true) Principal principal) {
        // Obtenemos el email del usuario logueado desde el token (Principal)
        solicitudService.crearSolicitud(dto, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // --- ENDPOINTS PARA ADMINISTRADORES ---

    @Operation(
            summary = "Listar solicitudes pendientes [ADMIN]",
            description = "Recupera todas las solicitudes de artista que se encuentran actualmente en estado pendiente de revisión. Requiere rol de administrador."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de solicitudes pendientes recuperada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SolicitudArtistaDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol de ADMINISTRADOR"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/admin/pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SolicitudArtistaDTO>> listarPendientes() {
        return ResponseEntity.ok(solicitudService.obtenerPendientes());
    }

    @Operation(
            summary = "Aprobar solicitud de artista [ADMIN]",
            description = "Permite actualizar los datos definitivos del perfil del artista y aprobar formalmente la solicitud, otorgando los permisos correspondientes. Requiere rol de administrador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud aprobada y perfil de artista consolidado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos o inconsistentes"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol de ADMINISTRADOR"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna solicitud con el ID proporcionado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/admin/aprobar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> actualizarYAprobar(
            @Parameter(description = "Identificador único de la solicitud a aprobar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos finales optimizados o corregidos para el alta definitiva del artista", required = true)
            @RequestBody SolicitudArtistaUpdateDTO dto) {
        solicitudService.actualizarYAprobar(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Rechazar solicitud de artista [ADMIN]",
            description = "Deniega la solicitud de verificación de artista asociada al identificador provisto. Requiere rol de administrador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud rechazada de forma exitosa"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado. Se requiere rol de ADMINISTRADOR"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna solicitud con el ID especificado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/admin/rechazar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rechazarSolicitud(
            @Parameter(description = "Identificador único de la solicitud a rechazar", example = "1", required = true)
            @PathVariable Long id) {
        solicitudService.rechazarSolicitud(id);
        return ResponseEntity.ok().build();
    }
}