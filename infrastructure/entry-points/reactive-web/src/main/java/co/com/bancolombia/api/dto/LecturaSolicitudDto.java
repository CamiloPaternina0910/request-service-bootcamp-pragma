package co.com.bancolombia.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LecturaSolicitudDto {

    private String id;

    private BigDecimal monto;

    private Integer plazo;

    private String correoElectronico;

    private String idEstado;

    private String idTipoPrestamo;
}
