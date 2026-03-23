package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.localidad.LocalidadCreateDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDTO;
import com.dawm.sonara.dtos.localidad.LocalidadDetailDTO;
import com.dawm.sonara.dtos.localidad.LocalidadUpdateDTO;
import com.dawm.sonara.repositories.LocalidadRepository;
import com.dawm.sonara.services.LocalidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
public class LocalidadController {
    private static final Logger logger = LoggerFactory.getLogger(LocalidadController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocalidadService localidadService;

    @Autowired
    private LocalidadRepository localidadRepository;
    /*
    @GetMapping
    public ResponseEntity<Page<LocalidadDTO>> listLocalidads(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        logger.info("Solicitando la lista de localidads... page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<LocalidadDTO> page = localidadService.list(pageable);

        logger.info("Se han cargado {} localidads en la pagina {}.",
                page.getNumberOfElements(), page.getNumber());
        return ResponseEntity.ok(page);
    }*/
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
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
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
                    description = "Localidad creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LocalidadDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<LocalidadDTO> createLocalidad(@Valid @RequestBody LocalidadCreateDTO dto) {

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
            description = "Permite actualizar los datos de una localidad existente en la base de datos."
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
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LocalidadDTO> updateUsuario(@PathVariable Long id,
                                                    @Valid @RequestBody LocalidadUpdateDTO localidadDTO) {
        logger.info("Actualizando localidad con ID {}", localidadDTO.getId());

        LocalidadDTO updated = localidadService.update(localidadDTO);
        logger.info("Localidad con ID {} actualizado con éxito", localidadDTO.getId());

        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar una localidad",
            description = "Permite eliminar una localidad específico de la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Localidad eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocalidad(@PathVariable Long id) {
        logger.info("Eliminando localidad con ID {}", id);

        localidadService.delete(id);

        logger.info("Localidad con ID {} eliminado con éxito.", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener una localidad por ID",
            description = "Recupera una localidad específico según su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Localidad encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LocalidadDetailDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LocalidadDetailDTO> getRegionById(@PathVariable Long id) {
        logger.info("Mostrando detalles de la localidad con ID {}", id);

        LocalidadDetailDTO localidadDetailDTO = localidadService.getDetail(id);

        return ResponseEntity.ok(localidadDetailDTO);
    }
}
