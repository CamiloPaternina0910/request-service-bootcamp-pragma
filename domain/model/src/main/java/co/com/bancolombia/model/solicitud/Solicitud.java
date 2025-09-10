package co.com.bancolombia.model.solicitud;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {

    private String id;

    private BigInteger monto;

    private Integer plazo;

    private String correoElectronico;

    private String documentoIdentificacion;

    private String idEstado;

    private String idTipoPrestamo;
}
