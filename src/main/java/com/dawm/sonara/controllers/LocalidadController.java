package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.localidad.LocalidadCreateDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDetailDTO;
import com.dawm.sonara.dtos.localidad.LocalidadUpdateDTO;
import com.dawm.sonara.repositories.LocalidadRepository;
import com.dawm.sonara.services.LocalidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/localidad")
@Tag(name = "Localidades", description = "Controlador para la gestión y consulta de las ubicaciones y localidades del sistema")
public class LocalidadController {
    private static final Logger logger = LoggerFactory.getLogger(LocalidadController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocalidadService localidadService;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Operation(
            summary = "Obtener todas las localidades",
            description = "Devuelve una lista paginada de todas las localidades disponibles en el sistema. " +
                    "Si se envía el parámetro 'unpaged=true', devuelve la lista completa sin paginación."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de localidades recuperada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LocalidadDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> listAllLocalidades(
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @Parameter(description = "Indica si se requiere la lista completa sin paginación", example = "false")
            @RequestParam(defaultValue = "false") boolean unpaged) {

        if (unpaged) {
            return ResponseEntity.ok(localidadService.listAll(Sort.by("name").ascending()));
        }

        return ResponseEntity.ok(localidadService.list(pageable));
    }

    @Operation(
            summary = "Crear una nueva localidad",
            description = "Permite registrar una nueva localidad en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Localidad creada exitosamente. Retorna la localización del nuevo recurso en el header.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LocalidadDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados para el registro"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<LocalidadDTO> createLocalidad(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estructura de datos para crear una localidad", required = true)
            @Valid @RequestBody LocalidadCreateDTO dto) {

        LocalidadDTO created = localidadService.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(
            summary = "Actualizar una localidad",
            description = "Permite actualizar los datos de una localidad existente en la base de datos localizándola mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Localidad actualizada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LocalidadDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos de modificación inválidos"),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada con el ID indicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LocalidadDTO> updateUsuario(
            @Parameter(description = "ID único de la localidad a modificar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados de la localidad", required = true)
            @Valid @RequestBody LocalidadUpdateDTO localidadDTO) {
        logger.info("Actualizando localidad con ID {}", localidadDTO.getId());

        LocalidadDTO updated = localidadService.update(localidadDTO);
        logger.info("Localidad con ID {} actualizado con éxito", localidadDTO.getId());

        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar una localidad",
            description = "Permite eliminar una localidad específica del registro de manera permanente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Localidad eliminada exitosamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocalidad(
            @Parameter(description = "ID único de la localidad a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        logger.info("Eliminando localidad con ID {}", id);

        localidadService.delete(id);

        logger.info("Localidad con ID {} eliminado con éxito.", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener una localidad por ID",
            description = "Recupera la información extendida de una localidad específica según su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Localidad encontrada con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LocalidadDetailDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada con el ID especificado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LocalidadDetailDTO> getRegionById(
            @Parameter(description = "ID único de la localidad a consultar", example = "1", required = true)
            @PathVariable Long id) {
        logger.info("Mostrando detalles de la localidad con ID {}", id);

        LocalidadDetailDTO localidadDetailDTO = localidadService.getDetail(id);

        return ResponseEntity.ok(localidadDetailDTO);
    }
}