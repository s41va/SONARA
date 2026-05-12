package com.dawm.sonara.entities;

import com.dawm.sonara.entities.enums.Estado;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "informacion_pago")
public class InformacionPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concierto_id")
    private Concierto concierto;

    @Column(name = "id_transaccion_stripe")
    private String idTransaccionStripe;

    @Column(name = "monto_pago")
    private BigDecimal montoPago;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING) // Para usar tu Enum
    @Column(name = "estado_pago")
    private Estado estadoPago;
}