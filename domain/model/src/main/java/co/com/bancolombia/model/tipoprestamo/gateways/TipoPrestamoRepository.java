package co.com.bancolombia.model.tipoprestamo.gateways;

import co.com.bancolombia.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {

    public Flux<TipoPrestamo> findAll();

    public Mono<TipoPrestamo>  findById(String id);
}
