package com.dawm.sonara.controllers;


import com.dawm.sonara.daos.GeneroDAO;
import com.dawm.sonara.dtos.GenerosDTO;
import com.dawm.sonara.dtos.GenerosDetailDTO;
import com.dawm.sonara.dtos.GenerosUpdateDTO;
import com.dawm.sonara.dtos.GenerosCreateDTO;
import com.dawm.sonara.entities.Genero;

import com.dawm.sonara.mappers.GeneroMapper;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/generos")
public class GeneroController {

    private static final Logger logger = LoggerFactory.getLogger(GeneroController.class);

    @Autowired
    private GeneroDAO generoDAO;

    @Autowired
    private MessageSource messageSource; // Para mensajes de internacionalización/error


    // --- MÉTODOS GET: LISTAR, DETALLE, NUEVO, EDITAR ---

    /**
     * Muestra la lista de todos los géneros. (Equivalente a doGet, action=list)
     * URL: /generos
     *
     * @param model El objeto Model para pasar datos a la vista.
     * @return La ruta a la vista JSP de lista de géneros.
     */
    @GetMapping
    public String listGeneros(Model model) {
        logger.info(" Solicitando la lista de todos los géneros...");
        List<Genero> listGenero = null;
        List<GenerosDTO> listGeneroDTOs = null;
        try {
            listGenero = generoDAO.listAllGeneros();
            listGeneroDTOs = GeneroMapper.toDTOList(listGenero);
            logger.info("Se han devuelto {} géneros.", listGeneroDTOs.size());
        } catch (Exception e) {
            logger.error(" Error al listar los géneros: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar los géneros.");
        }
        model.addAttribute("listGeneros", listGeneroDTOs);
        return "views/genero/genero-list";
    }

    /**
     * Muestra el detalle de un género específico.
     * URL: /generos/detail?id=X
     */
    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {
        logger.info("Mostrando detalle del género con ID: {}", id);
        try {
            Genero genero = generoDAO.getGeneroById(id);
            if (genero == null) {
                String msg = messageSource.getMessage("msg.genero-controller.detail.notFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", msg);
                return "redirect:/generos";
            }
            GenerosDetailDTO generoDTO = GeneroMapper.toDetailDTO(genero);
            model.addAttribute("genero", generoDTO);
            return "views/genero/genero-detail";
        } catch (Exception e) {
            logger.error("Error al obtener el detalle del género {} : {}", id, e.getMessage(), e);
            String msg = messageSource.getMessage("msg.genero-controller.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/generos";
        }
    }

    /**
     * Muestra el formulario para crear un nuevo género.
     * URL: /generos/new
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info(" Mostrando el formulario para nuevo género.");
        model.addAttribute("genero", new GenerosCreateDTO());
        return "views/genero/genero-form";
    }

    /**
     * Muestra el formulario para editar un género existente.
     * URL: /generos/edit?id=X
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info(" Entrando al método showEditForm para ID: {}", id);
        try {
            Genero genero = generoDAO.getGeneroById(id);
            GenerosUpdateDTO generoDTO = GeneroMapper.toUpdateDTO(genero);
            if (genero == null) {
                logger.warn(" No se ha encontrado el género con Id {}", id);
                String errorMessage = messageSource.getMessage("msg.genero-controller.edit.notFound", null, locale);
                model.addAttribute("errorMessage", errorMessage);
                model.addAttribute("genero", new GenerosUpdateDTO());
            } else {
                model.addAttribute("genero", generoDTO);
            }
        } catch (Exception e) {
            logger.error(" Error al obtener el género con Id {} :{}", id, e.getMessage());
            String errorMessage = messageSource.getMessage("msg.genero-controller.edit.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("genero", new GenerosUpdateDTO());
        }
        return "views/genero/genero-form";
    }

    // --- MÉTODOS POST: INSERTAR, ACTUALIZAR, ELIMINAR ---

    /**
     * Inserta un nuevo género en la base de datos.
     * URL: /generos/insert
     */
    @PostMapping("/insert")
    public String insertGenero(@Valid @ModelAttribute("genero")
                               GenerosCreateDTO generoDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes, Locale locale) {
        logger.info(" Intentando insertar nuevo género: {}", generoDTO.getNombre());

        try {
            if (result.hasErrors()) {
                // Si hay errores de validación (ej. @NotBlank), vuelve al formulario
                return "views/genero/genero-form";
            }

            // Validación de unicidad de nombre
            if (generoDAO.existsGeneroByName(generoDTO.getNombre())) {
                logger.warn("El nombre de género '{}' ya existe.", generoDTO.getNombre());
                String errorMessage = messageSource.getMessage("msg.genero-controller.insert.nameExist", null, locale);
                // Usamos addFlashAttribute para que el mensaje esté disponible después de la redirección
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/generos/new"; // Redirecciona para evitar el doble envío
            }

            Genero genero = GeneroMapper.toEntity(generoDTO);
            generoDAO.insertGenero(genero);
            logger.info(" Género '{}' insertado con éxito.", genero.getNombre());
            String successMessage = messageSource.getMessage("msg.genero-controller.insert.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (Exception e) {
            logger.error(" Error al insertar el género {}: {}", generoDTO.getNombre(), e.getMessage(), e);
            String errorMessage = messageSource.getMessage("msg.genero-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/generos/new";
        }
        return "redirect:/generos";
    }

    /**
     * Actualiza un género existente en la base de datos.
     * URL: /generos/update
     */
    @PostMapping("/update")
    public String updateGenero(@Valid @ModelAttribute("genero") GenerosUpdateDTO generoDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes, Locale locale) {
        logger.info(" Actualizando género con ID {}", generoDTO.getId());

        try {
            if (result.hasErrors()) {
                // Si hay errores de validación, vuelve al formulario
                return "views/genero/genero-form";
            }

            // Validación de unicidad de nombre (excluyendo el ID actual)
            if (generoDAO.existsGeneroByNameAndNotId(generoDTO.getNombre(), generoDTO.getId())) {
                logger.warn("El nombre de género '{}' ya existe para otro ID.", generoDTO.getNombre());
                String errorMessage = messageSource.getMessage("msg.genero-controller.update.nameExists", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                // Redirige al formulario de edición con el ID para conservar el contexto
                return "redirect:/generos/edit?id=" + generoDTO.getId();
            }

            Genero genero = GeneroMapper.toEntity(generoDTO);
            generoDAO.updateGenero(genero);
            logger.info(" Género con ID {} actualizado con éxito.", genero.getId());
            String successMessage = messageSource.getMessage("msg.genero-controller.update.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);


        } catch (Exception e) {
            logger.error(" Error al actualizar el género con ID {}: {}", generoDTO.getId(), e.getMessage(), e);
            String errorMessage = messageSource.getMessage("msg.genero-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/generos";
    }

    /**
     * Elimina un género de la base de datos.
     * URL: /generos/delete
     */
    @PostMapping("/delete")
    public String deleteGenero(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {
        logger.warn(" Entrando al método deleteGenero para ID: {}", id);

        try {
            // **Nota:** En un sistema real, antes de eliminar, se debería verificar
            // si existen canciones o álbumes asociados a este género.
            generoDAO.deleteGenero(id);
            logger.info(" Género con ID {} eliminado con éxito", id);
            String successMessage = messageSource.getMessage("msg.genero-controller.delete.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (Exception e) {
            logger.error(" Error al eliminar el género con ID {} : {}", id, e.getMessage(), e);
            String errorMessage = messageSource.getMessage("msg.genero-controller.delete.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/generos";
    }
}
