package co.com.bancolombia.r2dbc.solicitud.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;

@Table("solicitud")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudEntity {

    @Id
    private String id;

    private BigInteger monto;

    private Integer plazo;

    @Column("email")
    private String correoElectronico;

    private String idEstado;

    private String idTipoPrestamo;
}
