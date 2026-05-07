package com.dawm.sonara.services;

import com.dawm.sonara.dtos.solicitudArtista.*;
import java.util.List;

public interface SolicitudArtistaService {
    void crearSolicitud(SolicitudArtistaCreateDTO dto, String emailUsuario);
    List<SolicitudArtistaDTO> obtenerPendientes();
    void actualizarYAprobar(Long id, SolicitudArtistaUpdateDTO dto);
    void rechazarSolicitud(Long id);
}