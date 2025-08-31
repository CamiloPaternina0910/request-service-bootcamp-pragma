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
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        solicitudUseCase.findAll().map(solicitudMapper::toResponse)
                        , LecturaSolicitudDto.class
                );
    }
}
