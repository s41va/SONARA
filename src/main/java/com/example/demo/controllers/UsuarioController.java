package com.example.demo.controllers;

import com.example.demo.daos.LocalidadDAO;
import com.example.demo.daos.UsuarioDAO;
import com.example.demo.dtos.UsuarioCreateDTO;
import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.dtos.UsuarioDetailDTO;
import com.example.demo.dtos.UsuarioUpdateDTO;
import com.example.demo.entities.Localidad;
import com.example.demo.entities.Usuario;
import com.example.demo.mappers.UsuarioMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private LocalidadDAO localidadDAO;

    private List<String> getAllGeneros() {
        return List.of("Rock", "Pop", "Jazz", "Clásica", "Trap", "Reggaeton", "R&B", "Breakbeat", "Hip-Hop");
    }

    @GetMapping
    public String listUsuarios(@RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "10") int size,
                               @RequestParam(name = "sortField", defaultValue = "nombre") String sortField,
                               @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                               Model model,
                               Locale locale) {
        logger.info("Solicitando la lista de todos los usuarios... page={}, size={}, sortField={}, sortDir={}", page, size, sortField, sortDir);
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        try {
            long totalElements = usuarioDAO.countUsuarios();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            if (totalPages > 0 && page >= totalPages) {
                page = totalPages - 1;
            }
            int maxPagesToShow = 5;  // cantidad de páginas a mostrar en la paginación

            int startPage = Math.max(0, page - 2); // mostrar 2 páginas antes de la actual
            int endPage = Math.min(totalPages - 1, startPage + maxPagesToShow - 1);

            // Ajustar startPage si no hay suficientes páginas al final
            if (endPage - startPage + 1 < maxPagesToShow) {
                startPage = Math.max(0, endPage - maxPagesToShow + 1);
            }

            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            List<Usuario> listUsuarios = usuarioDAO.listUsuariosPage(page, size, sortField, sortDir);
            List<UsuarioDTO> listUsuariosDTO = UsuarioMapper.toDTOList(listUsuarios);
            logger.info("Se han cargado {} usuarios en la pagina {}.", listUsuariosDTO.size(), page);
            model.addAttribute("listUsuarios", listUsuariosDTO);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalElements", totalElements);
            //Para que la vista sepa como estamos ordenando ASC/DESC
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", "asc".equalsIgnoreCase(sortDir) ? "desc" : "asc");
        } catch (Exception e) {
            logger.error("Error al listar los usuarios", e);
            String errorMessage = messageSource.getMessage("msg.usuario-controller.list.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
        }

        return "views/usuario/usuario-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo usuario.");
        model.addAttribute("usuario", new UsuarioCreateDTO());
        model.addAttribute("allGeneros", getAllGeneros());
        model.addAttribute("allLocalidades", localidadDAO.listAllLocalidades());
        return "views/usuario/usuario-form";
    }

    @PostMapping("/insert")
    public String insertUsuario(
            @Valid @ModelAttribute("usuario") UsuarioCreateDTO usuarioDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Insertando nuevo usuario con email {}", usuarioDTO.getEmail());

        if (result.hasErrors()) {
            model.addAttribute("allGeneros", getAllGeneros());
            model.addAttribute("allLocalidades", localidadDAO.listAllLocalidades());
            return "views/usuario/usuario-form";
        }
        if (usuarioDTO.getFechaNacimiento() != null) {
            LocalDate fechaNacimiento = usuarioDTO.getFechaNacimiento();
            int year = fechaNacimiento.getYear();
            if (year < 1900 || year > LocalDate.now().getYear()) {
                result.rejectValue("fechaNacimiento", "error.fechaNacimiento", "Fecha de nacimiento inválida");
                model.addAttribute("allGeneros", getAllGeneros());
                model.addAttribute("allLocalidades", localidadDAO.listAllLocalidades());
                return "views/usuario/usuario-form";
            }
        }


        if (usuarioDAO.existUsuarioByEmail(usuarioDTO.getEmail())) {
            logger.warn("El usuario con email {} ya existe.", usuarioDTO.getEmail());
            model.addAttribute("allGeneros", getAllGeneros());
            model.addAttribute("allLocalidades", localidadDAO.listAllLocalidades());
            String errorMessage = messageSource.getMessage("msg.usuario-controller.insert.emailExist", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            return "views/usuario/usuario-form";
        }

        try {
            Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);

            // Buscar la localidad por id
            Long localidadId = usuarioDTO.getLocalidadId();
            Localidad localidad = localidadDAO.getLocalidadById(localidadId);
            if (localidad == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Localidad no encontrada");
                return "redirect:/usuario/new";
            }

            usuario.setLocalidad(localidad);
            usuario.setFechaRegistro(LocalDateTime.now());


            usuarioDAO.insertUsuario(usuario);
            logger.info("Usuario con email {} insertado con éxito.", usuario.getEmail());

        } catch (Exception e) {
            logger.error("Error al insertar el usuario", e);
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    messageSource.getMessage("msg.usuario-controller.insert.error", null, locale)
            );
            return "redirect:/usuario/new";
        }

        return "redirect:/usuario";
    }


    @PostMapping("/update")
    public String updateUsuario(@Valid @ModelAttribute("usuario") UsuarioUpdateDTO usuarioDTO,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                Locale locale) {
        logger.info("Actualizando usuario con ID {}", usuarioDTO.getId());
        if (result.hasErrors()) {
            model.addAttribute("allGeneros", getAllGeneros());
            model.addAttribute("allLocalidades", localidadDAO.listAllLocalidades());
            return "views/usuario/usuario-form";
        }
        try {
            if (usuarioDAO.existUsuarioByEmailAndNotId(usuarioDTO.getEmail(), usuarioDTO.getId())) {
                logger.warn("El email {} ya existe para otro usuario.", usuarioDTO.getEmail());
                model.addAttribute("allGeneros", getAllGeneros());
                model.addAttribute("allLocalidades", localidadDAO.listAllLocalidades());
                String errorMessage = messageSource.getMessage("msg.usuario-controller.update.emailExist", null, locale);
                model.addAttribute("errorMessage", errorMessage);
                return "views/usuario/usuario-form";
            }
            Usuario usuario = usuarioDAO.getUsuarioById(usuarioDTO.getId());

            if (usuario == null) {
                logger.warn("No se encontró el usuario con ID {}", usuarioDTO.getId());
                String notFound = messageSource.getMessage("msg.usuario-controller.detail.notFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", notFound);
                return "redirect:/usuario";
            }

            UsuarioMapper.copyToExistingEntity(usuarioDTO, usuario);

            // Asignar la localidad según el id del DTO
            Long localidadId = usuarioDTO.getLocalidadId();
            Localidad localidad = localidadDAO.getLocalidadById(localidadId);
            if (localidad == null) {
                String errorMessage = messageSource.getMessage("msg.usuario-controller.update.localidadNotFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/usuario/edit?id=" + usuarioDTO.getId();
            }
            usuario.setLocalidad(localidad);

            usuarioDAO.updateUsuario(usuario);
            logger.info("Usuario {} actualizado con éxito.", usuario.getId());
        } catch (Exception e) {
            logger.error("Error al actualizar el usuario con ID {}: {}", usuarioDTO.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.usuario-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/usuario";
    }


    @PostMapping("/delete")
    public String deleteUsuario(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando usuario con ID {}", id);
        try {
            usuarioDAO.deleteUsuario(id);
            logger.info("Usuario con ID {} eliminado con éxito.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar el usuario con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el usuario.");
        }
        return "redirect:/usuario";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando formulario de edición para el usuario con ID {}", id);
        try {
            Usuario usuario = usuarioDAO.getUsuarioById(id);
            UsuarioUpdateDTO usuarioDTO = UsuarioMapper.toUpdateDTO(usuario);
            if (usuarioDTO == null) {
                logger.warn("No se encontró el usuario con ID {}", id);
                String errorMessage = messageSource.getMessage("msg.usuario-controller.edit.notfound", null, locale);
                model.addAttribute("errorMessage", errorMessage);
                model.addAttribute("usuario", new UsuarioUpdateDTO());
            } else {
                model.addAttribute("usuario", usuarioDTO);
            }
            model.addAttribute("allGeneros", getAllGeneros());
            model.addAttribute("allLocalidades", localidadDAO.listAllLocalidades());
        } catch (Exception e) {
            logger.error("Error al obtener el usuario con ID {}: {}", id, e.getMessage());
            String errorMessage = messageSource.getMessage("msg.usuario-controller.edit.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("usuario", new UsuarioUpdateDTO());
        }
        return "views/usuario/usuario-form";
    }


    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {
        logger.info("Mostrando detalle del usuario con ID {}", id);
        try {
            Usuario usuario = usuarioDAO.getUsuarioById(id);
            if (usuario == null) {
                String msg = messageSource.getMessage("msg.usuario-controller.detail.notFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", msg);
                return "redirect:/usuario";
            }
            UsuarioDetailDTO usuarioDTO = UsuarioMapper.toDetailDTO(usuario);
            model.addAttribute("usuario", usuarioDTO);
            return "views/usuario/usuario-detail";
        } catch (Exception e) {
            logger.error("Error al obtener el detalle del usuario {}: {}", id, e.getMessage(), e);
            String msg = messageSource.getMessage("msg.usuario-controller.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/usuario";
        }
    }
}
