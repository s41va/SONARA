package com.dawm.sonara.dtos.artista;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArtistaRankingDTO {
    private String id;
    private String nombre;
    private String foto;
    private Long totalVotos;
}