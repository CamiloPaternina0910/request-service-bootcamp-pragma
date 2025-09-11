package co.com.bancolombia.model.solicitud.exception;

import co.com.bancolombia.model.exception.DominioException;

public class TipoPrestamoNoEncontradoException extends DominioException {

    private static final int HTTP_STATUS_NOT_FOUND = 404;

    public TipoPrestamoNoEncontradoException(String tipoPrestamo) {
        super(
                String.format("Tipo de prestamo (%s) no encontrado.",
                        tipoPrestamo), HTTP_STATUS_NOT_FOUND
        );
    }
}