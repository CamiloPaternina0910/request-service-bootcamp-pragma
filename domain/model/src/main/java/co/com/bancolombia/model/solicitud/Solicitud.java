package co.com.bancolombia.model.solicitud;
import co.com.bancolombia.model.estado.Estado;
import co.com.bancolombia.model.tipoprestamo.TipoPrestamo;
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

    private String plazo;

    private String correoElectronico;

    private Estado estado;

    private TipoPrestamo tipoPrestamo;
}
