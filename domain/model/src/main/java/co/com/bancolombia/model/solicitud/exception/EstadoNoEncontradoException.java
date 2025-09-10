package co.com.bancolombia.model.solicitud.exception;

import co.com.bancolombia.model.exception.DominioException;

public class EstadoNoEncontradoException extends DominioException {

    private static final int HTTP_STATUS_NOT_FOUND = 404;

    public EstadoNoEncontradoException(String estado) {
        super(
                String.format("Estado (%s) no encontrado.",
                        estado), HTTP_STATUS_NOT_FOUND
        );
    }
}