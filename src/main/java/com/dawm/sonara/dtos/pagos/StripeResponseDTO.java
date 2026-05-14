package com.dawm.sonara.dtos.pagos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripeResponseDTO {
    private String sessionId; //Para logs
    private String url;

    public StripeResponseDTO(String url) {
        this.url = url;
    }
}