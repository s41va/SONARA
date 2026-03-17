package com.dawm.sonara.repositories;

import com.dawm.sonara.dtos.generos.GenerosCreateDTO;
import com.dawm.sonara.dtos.generos.GenerosDTO;
import com.dawm.sonara.dtos.generos.GenerosDetailDTO;
import com.dawm.sonara.dtos.generos.GenerosUpdateDTO;
import com.dawm.sonara.entities.Genero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {


    boolean existsByNombre(String name) ;
    boolean existsByNombreAndIdNot(String name, Long id);
    List<Genero> findAll();
}
