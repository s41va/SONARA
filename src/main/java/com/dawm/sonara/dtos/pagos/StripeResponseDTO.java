package com.dawm.sonara.dtos.pagos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripeResponseDTO {
    private String intentId;
    private String clientSecret; // Este es el que necesita Angular
}