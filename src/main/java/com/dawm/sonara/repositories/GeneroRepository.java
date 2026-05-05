package com.dawm.sonara.repositories;

import com.dawm.sonara.entities.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {


    boolean existsByNombre(String name) ;
    boolean existsByNombreAndIdNot(String name, Long id);
    List<Genero> findAll();

    List<Genero> findAllByOrderByNombreAsc();
}
