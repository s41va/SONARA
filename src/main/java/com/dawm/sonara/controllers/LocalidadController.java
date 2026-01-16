package com.dawm.sonara.controllers;

import com.example.demo.daos.LocalidadDAO;
import com.dawm.sonara.dtos.LocalidadCreateDTO;
import com.dawm.sonara.dtos.LocalidadDTO;
import com.dawm.sonara.dtos.LocalidadDetailDTO;
import com.dawm.sonara.dtos.LocalidadUpdateDTO;
import com.dawm.sonara.entities.Localidad;
import jakarta.validation.Valid;
import com.dawm.sonara.mappers.LocalidadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/localidad")
public class LocalidadController {
    private static final Logger logger = LoggerFactory.getLogger(LocalidadController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocalidadDAO localidadDAO;

    @GetMapping
    public String listLocalidades(@RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name="size", defaultValue = "10") int size,
                                @RequestParam(name="sortField", defaultValue = "pais") String sortField,
                                @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                Model model,
                                Locale locale) {
        logger.info("Solicitando la lista de todas las localidades... page={}, size={}, sortField={}, sortDir={}", page, size, sortField, sortDir);
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        try {
            long totalElements = localidadDAO.countLocalidades();
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

            List<Localidad> listLocalidades = localidadDAO.listLocalidadesPage(page, size, sortField, sortDir);
            List<LocalidadDTO> listLocalidadesDTO = LocalidadMapper.toDTOList(listLocalidades);
            logger.info("Se han cargado {} localidades en la pagina {}.", listLocalidadesDTO.size(), page);
            model.addAttribute("listLocalidades", listLocalidadesDTO);
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalElements", totalElements);
            //Para que la vista sepa como estamos ordenando ASC/DESC
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", "asc".equalsIgnoreCase(sortDir) ? "desc" : "asc");
        }
        catch (Exception e) {
            logger.error("Error al listar las localidades", e);
            String errorMessage = messageSource.getMessage("msg.localidad-controller.list.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
        }

        return "views/localidad/localidad-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva localidad.");
        model.addAttribute("localidad", new LocalidadCreateDTO());
        return "views/localidad/localidad-form";
    }

    @PostMapping("/insert")
    public String insertLocalidad(
            @Valid @ModelAttribute("localidad") LocalidadCreateDTO localidadDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Insertando nueva localidad con codigo postal {}", localidadDTO.getCodigoPostal());

        if (result.hasErrors()) {
            return "views/localidad/localidad-form";
        }

        if (localidadDAO.existLocalidadByCodigoPostal(localidadDTO.getCodigoPostal())) {
            logger.warn("La localidad {} ya existe.", localidadDTO.getCodigoPostal());
            String errorMessage = messageSource.getMessage("msg.localidad-controller.insert.codeExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "views/localidad/localidad-form";
        }

        try {
            Localidad localidad = LocalidadMapper.toEntity(localidadDTO);
            localidadDAO.insertLocalidad(localidad);
            logger.info("Localidad con codigo postal {} insertada con éxito.", localidad.getCodigoPostal());

        } catch (Exception e) {
            logger.error("Error al insertar la localidad", e);
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    messageSource.getMessage("msg.localidad-controller.insert.error", null, locale)
            );
            return "redirect:/localidad/new";
        }

        return "redirect:/localidad";
    }

    @PostMapping("/update")
    public String updateLocalidad(@Valid @ModelAttribute("localidad") LocalidadUpdateDTO localidadDTO, BindingResult result,
                                  RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando localidad con ID {}", localidadDTO.getId());
        try {
            if (result.hasErrors()) {
                return "views/localidad/localidad-form";
            }
            if (localidadDAO.existLocalidadByCodigoPostalAndNotId(localidadDTO.getCodigoPostal(), localidadDTO.getId())) {
                logger.warn("El codigo postal {} ya existe para otro localidad.", localidadDTO.getCodigoPostal());
                String errorMessage = messageSource.getMessage("msg.localidad-controller.update.codeExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/localidad/edit?id=" + localidadDTO.getId();
            }
            Localidad localidad = localidadDAO.getLocalidadById(localidadDTO.getId());
            if (localidad == null){
                logger.warn("No se encontro el localidad con ID {}", localidadDTO.getId());
                String notFound = messageSource.getMessage("msg.localidad-controller.detail.notFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", notFound);
                return "redirect:/localidad";
            }
            LocalidadMapper.copyToExistingEntity(localidadDTO, localidad);
            localidadDAO.updateLocalidad(localidad);
            logger.info("Localidad {} actualizado con éxito.", localidad.getId());
        } catch (Exception e) {
            logger.error("Error al actualizar la localidad con ID {}: {}", localidadDTO.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.localidad-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/localidad";
    }
    
    @PostMapping("/delete")
    public String deleteLocalidad(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando localidad con ID {}", id);
        try {
            localidadDAO.deleteLocalidad(id);
            logger.info("Localidad con ID {} eliminado con éxito.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar el localidad con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el localidad.");
        }
        return "redirect:/localidad";
    }
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info("Mostrando formulario de edición para la localidad con ID {}", id);
        try {
            Localidad localidad = localidadDAO.getLocalidadById(id);
            LocalidadUpdateDTO localidadDTO = LocalidadMapper.toUpdateDTO(localidad);
            if (localidadDTO == null) {
                logger.warn("No se encontró la localidad con ID {}", id);
                String errorMessage = messageSource.getMessage("msg.localidad-controller.edit.notfound", null, locale);
                model.addAttribute("errorMessage", errorMessage);
                //En caso de error mandamos un DTO vacio para que la vista no reviente
                model.addAttribute("localidad", new LocalidadUpdateDTO());
            }
            else {
                model.addAttribute("localidad", localidadDTO);
            }
        } catch (Exception e) {
            logger.error("Error al obtener la localidad con ID {}: {}", id, e.getMessage());
            String errorMessage = messageSource.getMessage("msg.localidad-controller.edit.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("localidad", new LocalidadUpdateDTO());
        }
        return "views/localidad/localidad-form";
    }
    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {
        logger.info("Mostrando detalle de la localidad con ID {}", id);
        try {
            Localidad localidad = localidadDAO.getLocalidadById(id);
            if (localidad == null) {
                String msg = messageSource.getMessage("msg.localidad-controller.detail.notFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", msg);
                return "redirect:/localidad";
            }
            LocalidadDetailDTO localidadDTO = LocalidadMapper.toDetailDTO(localidad);
            model.addAttribute("localidad", localidadDTO);
            return "views/localidad/localidad-detail";
        } catch (Exception e) {
            logger.error("Error al obtener el detalle de la localidad {}: {}", id, e.getMessage(), e);
            String msg = messageSource.getMessage("msg.localidad-controller.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/localidad";
        }
    }

}
