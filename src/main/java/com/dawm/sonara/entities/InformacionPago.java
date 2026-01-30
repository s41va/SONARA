package com.dawm.sonara.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Informacion_Pago")
public class InformacionPago {

//    BigDecimal: Nunca uses Float o Double para precios o montos financieros;
//    BigDecimal junto con los parámetros precision y scale asegura que los céntimos no se pierdan en redondeos extraños.


    @EmbeddedId
    private InformacionPagoId id;

    // El campo id_pago no es la PK según tu SQL, pero está en la tabla
    @Column(name = "id_pago")
    private Integer idPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("conciertoId")
    @JoinColumn(name = "concierto_id")
    private Concierto concierto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    @Column(name = "id_transaccion", length = 100)
    private String idTransaccion;

    @Column(name = "monto_pago", precision = 10, scale = 2)
    private BigDecimal montoPago;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Column(name = "estado_pago", length = 50)
    private String estadoPago;

    @Column(name = "detalles_pago", columnDefinition = "TEXT")
    private String detallesPago;
}