package co.com.bancolombia.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CrearSolicitudDto {

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 0, message = "El monto debe ser mayor a 0")
    @Schema(description = "Monto del prestamo", example = "3500000")
    private BigDecimal monto;

    @NotNull(message = "El plazo es obligatorio")
    @Min(value = 0, message = "El plazo debe ser mayor a 0")
    @Schema(description = "Plazo del prestamo", example = "1")
    private Integer plazo;

    @NotBlank(message = "El tipo de prestamo es obligatorio")
    @Schema(description = "Tipo de prestamo", example = "HIPOTECARIO")
    private String idTipoPrestamo;
}
