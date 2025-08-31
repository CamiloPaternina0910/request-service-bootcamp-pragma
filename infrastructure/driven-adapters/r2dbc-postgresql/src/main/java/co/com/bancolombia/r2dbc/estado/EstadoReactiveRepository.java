package co.com.bancolombia.r2dbc.estado;

import co.com.bancolombia.model.estado.Estado;
import co.com.bancolombia.r2dbc.estado.entity.EstadoEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface EstadoReactiveRepository extends ReactiveCrudRepository<EstadoEntity, String>, ReactiveQueryByExampleExecutor<EstadoEntity> {
    Mono<Estado> findByNombre(String nombre);
}
