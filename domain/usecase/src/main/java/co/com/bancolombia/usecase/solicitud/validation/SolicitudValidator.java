package co.com.bancolombia.usecase.solicitud.validation;

import co.com.bancolombia.model.estado.gateways.EstadoRepository;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudValidator {

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;
    
    public Mono<Void> validarCreacionSolicitud(Solicitud solicitud){
        return null;
    }
}
