package com.dawm.sonara.daos;




import com.dawm.sonara.entities.Artista;

import java.util.List;

public interface ArtistaDAO {

    List<Artista> listAllArtist();
    void insertArtista(Artista ar) ;
    void deleteArtista(long id);
    void updateArtista(Artista ar);
    Artista getArtistaById(Long id);
    Artista getArtistaByName(String name);
    Boolean existsArtistaByName(String name);
    Boolean existsArtistaByNameAndNotId(String name, Long id);
}
