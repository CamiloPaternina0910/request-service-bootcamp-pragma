package co.com.bancolombia.usecase.solicitud;

import co.com.bancolombia.model.jwt.gateway.JwtService;
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
    private final JwtService jwtService;

    public Mono<Solicitud> save(Solicitud solicitud){
        return jwtService.getPrincipal()
                        .flatMap(documentIdentification -> {
                            solicitud.setDocumentoIdentificacion(documentIdentification);
                            return solicitudValidator.validarCreacionSolicitud(solicitud)
                                    .flatMap(solicitudRepository::save);
                        });
    }

    public Flux<Solicitud> findAll(Solicitud filtros, Integer numeroPagina, Integer tamanoPagina){
        return solicitudRepository.findAll(filtros, numeroPagina, tamanoPagina);
    }

    public Mono<Long> count(Solicitud filtros){
        return solicitudRepository.count(filtros);
    }
}
