package co.com.bancolombia.model.solicitud.gateways;

import co.com.bancolombia.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitudRepository {


    public Mono<Solicitud> save(Solicitud solicitud);

    public Flux<Solicitud> findAll();

    public Mono<Solicitud> findById(String id);

    public Mono<Solicitud> update(Solicitud solicitud);

    public Mono<Void> deleteById(String id);
}
