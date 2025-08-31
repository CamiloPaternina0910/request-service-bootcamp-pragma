package co.com.bancolombia.r2dbc.tipoPrestamo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("tipo_prestamo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoPrestamoEntity {
    @Id
    private String id;

    private String nombre;

    @Column("monto_minimo")
    private BigDecimal montoMinimo;

    @Column("monto_maximo")
    private BigDecimal montoMaximo;

    @Column("tasa_interes")
    private Float tasaInteres;

    @Column("validacion_automatica")
    private Boolean validacionAutomatica;
}
