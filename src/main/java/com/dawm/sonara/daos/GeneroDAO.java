package com.dawm.sonara.daos;


import com.dawm.sonara.entities.Genero;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface GeneroDAO {

    List<Genero> listAllGeneros();
    void insertGenero(Genero genero) ;
    void deleteGenero(long id);
    void updateGenero(Genero genero);
    Genero getGeneroById(Long id);
    Genero getGeneroByName(String name);
    Boolean existsGeneroByName(String name);
    Boolean existsGeneroByNameAndNotId(String name, Long id);
}
