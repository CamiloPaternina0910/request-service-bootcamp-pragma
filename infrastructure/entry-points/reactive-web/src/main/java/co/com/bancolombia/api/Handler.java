package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.CrearSolicitudDto;
import co.com.bancolombia.api.dto.LecturaSolicitudDto;
import co.com.bancolombia.api.mapper.SolicitudMapper;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.usecase.solicitud.SolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

@Component
@RequiredArgsConstructor
public class Handler {

    private final SolicitudUseCase solicitudUseCase;
    private final SolicitudMapper solicitudMapper;
    private final ReactiveValidator reactiveValidator;


    public Mono<ServerResponse> listenSaveRequest(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CrearSolicitudDto.class)
                .flatMap(reactiveValidator::validate)
                .map(solicitudMapper::toModel)
                .flatMap(solicitudUseCase::save)
                .map(solicitudMapper::toResponse)
                .flatMap(saved -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(saved));
    }

    public Mono<ServerResponse> listenFindAllRequest(ServerRequest serverRequest) {
        Solicitud filtros = getFiltrosFindAll(serverRequest);
        Integer numeroPagina = serverRequest.queryParam("numeroPagina")
                .map(Integer::parseInt)
                .orElse(0);
        Integer tamanoPagina = serverRequest.queryParam("tamanoPagina")
                .map(Integer::parseInt)
                .orElse(20);

        return solicitudUseCase.count(filtros).flatMap(c -> {
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Total-Count", String.valueOf(c))
                    .header("X-Page-Number", String.valueOf(numeroPagina))
                    .header("X-Page-Size", String.valueOf(tamanoPagina))
                    .body(solicitudUseCase.findAll(filtros, numeroPagina, tamanoPagina)
                            .map(solicitudMapper::toResponse), LecturaSolicitudDto.class);
        });
    }

    private Solicitud getFiltrosFindAll(ServerRequest serverRequest) {
        String montoStr = serverRequest.queryParam("monto").orElse(null);
        String plazoStr = serverRequest.queryParam("plazo").orElse(null);
        String correoElectronico = serverRequest.queryParam("correoElectronico").orElse(null);
        String idEstado = serverRequest.queryParam("idEstado").orElse(null);
        String idTipoPrestamo = serverRequest.queryParam("idTipoPrestamo").orElse(null);


        return Solicitud.builder()
                .monto(montoStr != null ? new BigInteger(montoStr) : null)
                .plazo(plazoStr != null ? Integer.parseInt(plazoStr) : null)
                .correoElectronico(correoElectronico)
                .idEstado(idEstado)
                .idTipoPrestamo(idTipoPrestamo)
                .build();
    }
}
