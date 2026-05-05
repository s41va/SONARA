package com.dawm.sonara.services;

import com.dawm.sonara.dtos.artista.ArtistaRankingDTO;
import com.dawm.sonara.entities.Usuario;
import java.util.List;

public interface VotoService {
    void votar(String artistaId, Usuario usuario);
    List<ArtistaRankingDTO> getRankingLocal(String ciudad);
    List<ArtistaRankingDTO> getRankingGlobal();
}