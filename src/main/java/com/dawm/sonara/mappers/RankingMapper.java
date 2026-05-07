package com.dawm.sonara.mappers;

import com.dawm.sonara.dtos.artista.ArtistaRankingDTO;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RankingMapper {

    public ArtistaRankingDTO toDTO(Object[] row) {
        if (row == null) return null;

        // Extraemos el conteo de votos de forma ultra segura
        // res[3] es el COUNT() de la query
        Number totalVotos = (row[3] != null) ? (Number) row[3] : 0L;

        return new ArtistaRankingDTO(
                (String) row[0], // id
                (String) row[1], // nombre
                (String) row[2], // foto
                totalVotos.longValue() // Esto convierte Integer/BigInteger/Long a Long sin petar
        );
    }

    public List<ArtistaRankingDTO> toDTOList(List<Object[]> rows) {
        return rows.stream()
                .map(this::toDTO)
                .toList();
    }
}