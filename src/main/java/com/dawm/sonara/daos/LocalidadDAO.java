package com.dawm.sonara.daos;

import com.dawm.sonara.entities.Localidad;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LocalidadDAO {
    List<Localidad> listAllLocalidades();
    List<Localidad> listLocalidadesPage(int page, int size, String sortField, String sortDir);
    long countLocalidades();
    void insertLocalidad(Localidad province);
    void updateLocalidad(Localidad province);
    void deleteLocalidad(Long id);
    Localidad getLocalidadById(Long id);
    boolean existLocalidadByCodigoPostal(String codigoPostal);
    boolean existLocalidadByCodigoPostalAndNotId(String codigoPostal, Long id);

}