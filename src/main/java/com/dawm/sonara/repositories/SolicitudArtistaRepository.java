package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.SolicitudArtista;
import com.dawm.sonara.entities.enums.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitudArtistaRepository extends JpaRepository<SolicitudArtista, Long> {
    // Para que el admin vea lo que tiene pendiente de revisar
    List<SolicitudArtista> findByEstado(EstadoSolicitud estado);
}