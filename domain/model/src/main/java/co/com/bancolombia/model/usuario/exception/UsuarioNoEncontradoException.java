package co.com.bancolombia.model.usuario.exception;

import co.com.bancolombia.model.exception.DominioException;

public class UsuarioNoEncontradoException extends DominioException {

    private static final int HTTP_STATUS_NOT_FOUND = 404;

    public UsuarioNoEncontradoException(String documentoIdentificacion){
        super(
                String.format("Usuario con documento de identificación %s no encontrado.",
                        documentoIdentificacion), HTTP_STATUS_NOT_FOUND
        );
    }
}
