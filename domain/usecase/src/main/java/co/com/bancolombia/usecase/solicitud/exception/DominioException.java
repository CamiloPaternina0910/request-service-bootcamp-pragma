package co.com.bancolombia.usecase.solicitud.exception;

import lombok.Getter;

@Getter
public class DominioException extends RuntimeException{

    private final int estado;

    public DominioException(String mensaje, int estado){
        super(mensaje);
        this.estado = estado;
    }
}
