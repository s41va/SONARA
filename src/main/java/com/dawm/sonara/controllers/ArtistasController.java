package com.dawm.sonara.controllers;


import com.dawm.sonara.daos.ArtistaDAO;
import com.dawm.sonara.dtos.ArtistasCreateDTO;
import com.dawm.sonara.dtos.ArtistasDTO;
import com.dawm.sonara.dtos.ArtistasDetailDTO;
import com.dawm.sonara.dtos.ArtistasUpdateDTO;
import com.dawm.sonara.entities.Artista;
import jakarta.validation.Valid;
import com.dawm.sonara.mappers.ArtistasMapper;
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
@RequestMapping("/artistas")
public class ArtistasController {


    private static final Logger logger = LoggerFactory.getLogger(ArtistasController.class);

    @Autowired
    private ArtistaDAO artistaDAO;


    @Autowired
    private MessageSource messageSource; // Para mensajes de internacionalización/error

    // --- MÉTODOS GET: LISTAR, NUEVO, EDITAR ---

    /**
     * Muestra la lista de todos los usuarios. (Equivalente a doGet, action=list)
     * URL: /users
     *
     * @param model El objeto Model para pasar datos a la vista.
     * @return La ruta a la vista JSP de lista de usuarios.
     */


    @GetMapping
    public String listArtistas(Model model) {
        logger.info(" Solicitando la lista de todos los usuarios...");
        List<Artista> listArtistas = null;
        List<ArtistasDTO> listArtistasDTOs = null;
        try {
            listArtistas = artistaDAO.listAllArtist();
            listArtistasDTOs = ArtistasMapper.toDTOList(listArtistas);
            logger.info("Se han devuelto {} usuarios.", listArtistasDTOs.size());
        } catch (Exception e) {
            logger.error(" Error al listar los usuarios: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar los usuarios.");
        }
        model.addAttribute("listArtistas", listArtistasDTOs);
        return "views/artista/artista-list";
    }
    @GetMapping("/detail")
    public String showDetail(@RequestParam("id")Long id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale){
        logger.info("Mostrando detalle del artista con ID: {}", id);
        try{
            Artista artista = artistaDAO.getArtistaById(id);
            if (artista == null){
                String msg = messageSource.getMessage("msg.artista-controller.detail.notFound", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", msg);
                return "redirect:/artistas";
            }
            ArtistasDetailDTO artistaDTO = ArtistasMapper.toDetailDTO(artista);
            model.addAttribute("artista", artistaDTO);
            return "views/artista/artista-detail";
        }catch (Exception e){
            logger.error("Error al obtener el detalle del artista {} : {}", id, e.getMessage(),e);
            String msg = messageSource.getMessage("msg.artista-controller.detail.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/artistas";
        }
    }


   /* *
     * Muestra el formulario para crear un nuevo usuario. (Equivalente a doGet, action=new)
     * URL: /users/new
     *
     * @param model El objeto Model para pasar un objeto `Users` vacío al formulario.
     * @return La ruta a la vista JSP del formulario de usuario.*/

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info(" Mostrando el formulario para nuevo usuario.");
        // Se crea un objeto Users vacío para enlazar los datos del formulario
        model.addAttribute("artista", new ArtistasCreateDTO());
        return "views/artista/artista-form";
    }

    /*
     * Muestra el formulario para editar un usuario existente. (Equivalente a doGet, action=edit)
     * URL: /users/edit?id=X
     *
     * @param id El ID del usuario a editar, tomado del parámetro de la URL.
     * @param model El objeto Model para pasar el usuario a la vista.
     * @return La ruta a la vista JSP del formulario de usuario.
    */

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model, Locale locale) {
        logger.info(" Entrando al método showEditForm para ID: {}", id);
        try {
            Artista artista = artistaDAO.getArtistaById(id);
            ArtistasUpdateDTO artistaDTO = ArtistasMapper.toUpdateDTO(artista);
            if (artista == null) {
                logger.warn(" No se ha encontrado el artista con Id {}", id);
                String errorMessage = messageSource.getMessage("msg.artista-controller.edit.notFound", null, locale);
                model.addAttribute("errorMessage", errorMessage);
                model.addAttribute("artista", new ArtistasUpdateDTO());

            }else{
                model.addAttribute("artista", artistaDTO);
            }
        } catch (Exception e) {
            logger.error(" Error al obtener el usuario con Id {} :{}", id, e.getMessage());
            String errorMessage = messageSource.getMessage("msg.artista-controller.edit.error", null, locale);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("artista", new ArtistasUpdateDTO());
        }
        return "views/artista/artista-form";
    }

    // --- MÉTODOS POST: INSERTAR, ACTUALIZAR, ELIMINAR ---

    /*
     * Inserta un nuevo usuario en la base de datos. (Equivalente a doPost, action=insert)
     * URL: /users/insert
     *
     * @param userDTO El objeto Users con los datos del formulario (debe tener las validaciones JSR-303).
     * @param result El resultado del proceso de validación.
     * @param redirectAttributes Atributos para mensajes flash (mensajes de éxito/error después de la redirección).
     * @param locale La configuración regional para mensajes internacionalizados.
     * @return Redirección a la lista de usuarios.

     */

    @PostMapping("/insert")
    public String insertArtista(@ModelAttribute("artista")
                              ArtistasCreateDTO artistaDTO,
                              Model model,
                              BindingResult result,
                              RedirectAttributes redirectAttributes, Locale locale) {
        logger.info(" Insertando nuevo usuario: {}", artistaDTO.getNombre_artistico());


        try {

            if (result.hasErrors()) {
                return "artista-form"; // Vuelve al formulario con errores de campo
            }

            // **Validación de unicidad de username**
            if (artistaDAO.existsArtistaByName(artistaDTO.getNombre_artistico())) {
                logger.warn("El nombre {} ya existe.", artistaDTO.getNombre_artistico());
                // Usar messageSource para el mensaje de error si está configurado
                String errorMessage = messageSource.getMessage("msg.artista-controller.insert.nameExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage); // Mantener datos
                return "redirect:/artistas/new";
            }

            Artista artista = ArtistasMapper.toEntity(artistaDTO);
            artistaDAO.insertArtista(artista);
            logger.info(" Usuario '{}' insertado con éxito.", artista.getNombre_artistico());
            String successMessage = messageSource.getMessage("msg.artista-controller.insert.success", null, locale);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (Exception e) {
            logger.error(" Error al insertar el usuario {}: {}", artistaDTO.getNombre_artistico(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.artista-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);


            return "redirect:/artistas/new";
        }
        return "redirect:/artistas";
    }

    /*
     * Actualiza un usuario existente en la base de datos. (Equivalente a doPost, action=update)
     * URL: /users/update
     *
     * @param userDTO Objeto Users con los datos actualizados.
     * @param result Resultado de la validación.
     * @param redirectAttributes Atributos para mensajes flash.
     * @param locale Configuración regional.
     * @return Redirección a la lista de usuarios.

     */

    @PostMapping("/update")
    public String updateUsers(@Valid @ModelAttribute("artista") ArtistasUpdateDTO artistasDTO,
                              BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info(" Actualizando usuario con ID {}", artistasDTO.getArtista_id());

        try {
            // **Validaciones JSR-303 (si estuvieran implementadas en Users.java)**
            if (result.hasErrors()) {
                return "views/artistas/artista-form"; // Vuelve al formulario con errores de campo
            }
            // **Validación de unicidad de username (excluyendo el ID actual)**
            if (artistaDAO.existsArtistaByNameAndNotId(artistasDTO.getNombre_artistico(), artistasDTO.getArtista_id())) {
                logger.warn("El nombre artistico {} ya existe para otro artista.", artistasDTO.getNombre_artistico());
                String errorMessage = messageSource.getMessage("msg.artista-controller.update.artistaExists", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/artistas/edit?id=" + artistasDTO.getArtista_id();
            }

            Artista artista = ArtistasMapper.toEntity(artistasDTO);
            artistaDAO.updateArtista(artista);
            logger.info(" Artista con ID {} actualizado con éxito.", artista.getArtista_id());


        } catch (Exception e) {
            logger.error(" Error al actualizar el usuario con ID {}: {}", artistasDTO.getArtista_id(), e.getMessage());
            String errorMessage = messageSource.getMessage("msg.artista-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

        }
        return "redirect:/artistas";
    }

     /* Elimina un usuario de la base de datos. (Equivalente a doPost/deleteUsers)
     * URL: /users/delete
     *
     * @param id El ID del usuario a eliminar.
     * @param redirectAttributes Atributos para mensajes flash.
     * @return Redirección a la lista de usuarios.
      */


    @PostMapping("/delete")
    public String deleteArtistas(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.warn(" Entrando al método deleteArtistas para ID: {}", id);

        try {
            artistaDAO.deleteArtista(id);
            logger.info(" Artista con ID {} eliminado con éxito", id);
            redirectAttributes.addFlashAttribute("successMessage", "Artista eliminado con éxito.");
        } catch (Exception e) {
            logger.error(" Error al eliminar el artista con ID {} : {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el artista.");
        }
        return "redirect:/artistas";
    }
}
