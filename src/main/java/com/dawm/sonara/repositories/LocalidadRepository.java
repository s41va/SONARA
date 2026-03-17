package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {

    boolean existsByCodigoPostal(String codigoPostal);

    boolean existsByCodigoPostalAndIdNot(String codigoPostal, Long id);


    Optional<Localidad> findByCodigoPostal(String codigoPostal);

    List<Localidad> findByIdNot(Long id);

}
