package co.com.bancolombia.r2dbc.estado;

import co.com.bancolombia.model.estado.Estado;
import co.com.bancolombia.model.estado.gateways.EstadoRepository;
import co.com.bancolombia.r2dbc.estado.entity.EstadoEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class EstadoReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Estado,
        EstadoEntity,
        String,
        EstadoReactiveRepository
        > implements EstadoRepository {
    public EstadoReactiveRepositoryAdapter(EstadoReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Estado.class));
    }

    @Override
    public Mono<Estado> findByNombre(String nombre) {
        log.info("Buscando estado por nombre: {}", nombre);
        return this.repository.findByNombre(nombre)
                .doOnNext(estado -> log.info("Estado encontrado {}", estado.getId()))
                .doOnError(e -> log.error("No se pudo encontrar el estado por: {}", e.getMessage()));
    }
}
