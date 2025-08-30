package co.com.bancolombia.model.estado.gateways;

import co.com.bancolombia.model.estado.Estado;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EstadoRepository {

    public Flux<Estado> findAll();

    public Mono<Estado> findById(String id);
}
