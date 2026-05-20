package com.dawm.sonara.controllers;

import com.dawm.sonara.dtos.usuario.UsuarioCreateDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDTO;
import com.dawm.sonara.dtos.usuario.UsuarioDetailDTO;
import com.dawm.sonara.dtos.usuario.UsuarioUpdateDTO;
import com.dawm.sonara.entities.Roles;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.repositories.LocalidadRepository;
import com.dawm.sonara.repositories.RolesRepository;
import com.dawm.sonara.services.LocalidadService;
import com.dawm.sonara.services.UsuarioService;
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
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Controlador para la administración, registro y consulta del ciclo de vida de los usuarios y sus roles")
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private LocalidadService localidadService;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Devuelve una lista paginada de todos los usuarios disponibles en el sistema. " +
                    "Si se envía el parámetro 'unpaged=true', devuelve la lista completa sin paginación."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuarios recuperada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<?> listAllUsuarios(
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @Parameter(description = "Indica si se requiere la lista completa sin aplicar límites de paginación", example = "false")
            @RequestParam(defaultValue = "false") boolean unpaged) {

        if (unpaged) {
            return ResponseEntity.ok(usuarioService.listAll(Sort.by("name").ascending()));
        }

        return ResponseEntity.ok(usuarioService.list(pageable));
    }

    @Operation(
            summary = "Crear un nuevo usuario",
            description = "Permite registrar un nuevo usuario en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado exitosamente. Retorna la localización del nuevo recurso en la cabecera.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados para el registro"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Estructura de datos para crear un usuario", required = true)
            @Valid @RequestBody UsuarioCreateDTO dto) {

        UsuarioDTO created = usuarioService.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(
            summary = "Actualizar un usuario",
            description = "Permite actualizar los datos de un usuario existente en la base de datos, incluyendo la asignación de sus roles correspondientes."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos de modificación inválidos proporcionados"),
            @ApiResponse(responseCode = "404", description = "Usuario o identificadores de roles no encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(
            @Parameter(description = "ID único del usuario a modificar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos modificados del usuario junto con su listado de roles", required = true)
            @Valid @RequestBody UsuarioUpdateDTO usuarioDTO) {
        logger.info("Actualizando usuario con ID {}", usuarioDTO.getId());
        if (usuarioDTO.getRolesIds() != null && usuarioDTO.getRolesIds().isEmpty()) {
            usuarioDTO.setRolesIds(null);
        }
        // Filtrar nulls de roleIds
        Set<Long> roleIds = new HashSet<>();
        if (usuarioDTO.getRolesIds() != null) {
            for (Long roleId : usuarioDTO.getRolesIds()) {
                if (roleId != null) {
                    roleIds.add(roleId);
                }
            }
        }
        // Obtener roles existentes
        Set<Roles> roles = new HashSet<>(rolesRepository.findAllById(roleIds));

        // Validar que se encontraron todos los roles
        if (roles.size() != roleIds.size()) {
            throw new ResourceNotFoundException("roles", "ids", roleIds);
        }

        UsuarioDTO updated = usuarioService.update(usuarioDTO, roles);
        logger.info("Usuario con ID {} actualizado con éxito", usuarioDTO.getId());

        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Eliminar un usuario",
            description = "Permite eliminar un usuario específico de la base de datos de manera permanente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente (Sin contenido)"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(description = "ID único del usuario a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        logger.info("Eliminando usuario con ID {}", id);

        usuarioService.delete(id);

        logger.info("Usuario con ID {} eliminado con éxito.", id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener un usuario por ID",
            description = "Recupera la información extendida y detallada de un usuario específico según su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDetailDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con el ID especificado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetailDTO> getUsuarioById(
            @Parameter(description = "ID único del usuario a consultar", example = "1", required = true)
            @PathVariable Long id) {
        logger.info("Mostrando detalle del usuario con ID {}", id);

        UsuarioDetailDTO usuarioDetailDTO = usuarioService.getDetail(id);

        return ResponseEntity.ok(usuarioDetailDTO);
    }
}