package co.com.bancolombia.usecase.solicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.usecase.solicitud.validation.SolicitudValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final SolicitudValidator solicitudValidator;

    public Mono<Solicitud> save(Solicitud solicitud){
        return solicitudValidator.validarCreacionSolicitud(solicitud)
                .flatMap(solicitudRepository::save);
    }

    public Flux<Solicitud> findAll(){
        return solicitudRepository.findAll();
    }
}
