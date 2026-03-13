package com.dawm.sonara.controllers;



import com.dawm.sonara.dtos.generos.GenerosCreateDTO;
import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.dtos.generos.GenerosDetailDTO;
import com.dawm.sonara.dtos.generos.GenerosUpdateDTO;
import com.dawm.sonara.exceptions.DuplicateResourceException;
import com.dawm.sonara.exceptions.ResourceNotFoundException;
import com.dawm.sonara.services.GeneroService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
@RequestMapping("/generos")
public class GeneroController {

    private static final Logger logger = LoggerFactory.getLogger(GeneroController.class);

    @Autowired
    private GeneroService generoService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista los géneros con paginación y ordenación.
     */
    @GetMapping
    public String listGeneros(
            @PageableDefault(size = 10, sort = "nombre", direction = Sort.Direction.ASC) Pageable pageable,
            Model model) {

        logger.info("Listando géneros page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        try {
            Page<GenerosDTO> page = generoService.list(pageable);
            model.addAttribute("page", page);

            String sortParam = "nombre,asc";
            if (page.getSort().isSorted()) {
                Sort.Order order = page.getSort().iterator().next();
                sortParam = order.getProperty() + "," + order.getDirection().name().toLowerCase();
            }
            model.addAttribute("sortParam", sortParam);

        } catch (Exception e) {
            logger.error("Error al listar los géneros: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Error al listar los géneros.");
        }

        return "views/genero/genero-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo género.");
        model.addAttribute("genero", new GenerosCreateDTO());
        return "views/genero/genero-form";
    }

    @PostMapping("/insert")
    public String insertGenero(@Valid @ModelAttribute("genero") GenerosCreateDTO generoDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {

        logger.info("Insertando nuevo género: {}", generoDTO.getNombre());

        if (result.hasErrors()) {
            return "views/genero/genero-form";
        }

        try {
            generoService.create(generoDTO);
            String successMessage = messageSource.getMessage("msg.genero-controller.insert.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/generos";

        } catch (DuplicateResourceException ex) {
            logger.warn("El nombre del género {} ya existe.", generoDTO.getNombre());
            String errorMessage = messageSource.getMessage("msg.genero-controller.insert.nameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/generos/new";

        } catch (Exception e) {
            logger.error("Error al insertar género {}: {}", generoDTO.getNombre(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.genero-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/generos/new";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Mostrando formulario de edición ID {}", id);
        try {
            GenerosUpdateDTO generoDTO = generoService.getForEdit(id);
            model.addAttribute("genero", generoDTO);
            return "views/genero/genero-form";

        } catch (ResourceNotFoundException ex) {
            String msg = messageSource.getMessage("msg.genero-controller.edit.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/generos";

        } catch (Exception e) {
            logger.error("Error al obtener género ID {}: {}", id, e.getMessage());
            String msg = messageSource.getMessage("msg.genero-controller.edit.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/generos";
        }
    }

    @PostMapping("/update")
    public String updateGenero(@Valid @ModelAttribute("genero") GenerosUpdateDTO generoDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {

        logger.info("Actualizando género ID {}", generoDTO.getId());

        if (result.hasErrors()) {
            return "views/genero/genero-form";
        }

        try {
            generoService.update(generoDTO);
            String successMessage = messageSource.getMessage("msg.genero-controller.update.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/generos";

        } catch (DuplicateResourceException ex) {
            String errorMessage = messageSource.getMessage("msg.genero-controller.update.nameExists", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/generos/edit?id=" + generoDTO.getId();

        } catch (ResourceNotFoundException ex) {
            String msg = messageSource.getMessage("msg.genero-controller.edit.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/generos";

        } catch (Exception e) {
            logger.error("Error al actualizar género ID {}: {}", generoDTO.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.genero-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/generos/edit?id=" + generoDTO.getId();
        }
    }

    @PostMapping("/delete")
    public String deleteGenero(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Eliminando género ID {}", id);
        try {
            generoService.delete(id);
            String successMessage = messageSource.getMessage("msg.genero-controller.delete.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/generos";

        } catch (ResourceNotFoundException ex) {
            String msg = messageSource.getMessage("msg.genero-controller.edit.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/generos";

        } catch (Exception e) {
            logger.error("Error al eliminar género ID {}: {}", id, e.getMessage());
            String errorMessage = messageSource.getMessage("msg.genero-controller.delete.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/generos";
        }
    }

    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {
        try {
            GenerosDetailDTO detailDTO = generoService.getDetail(id);
            model.addAttribute("genero", detailDTO);
            return "views/genero/genero-detail";

        } catch (ResourceNotFoundException ex) {
            String msg = messageSource.getMessage("msg.genero-controller.detail.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/generos";

        } catch (Exception e) {
            logger.error("Error detalle género ID {}: {}", id, e.getMessage());
            return "redirect:/generos";
        }
    }
}