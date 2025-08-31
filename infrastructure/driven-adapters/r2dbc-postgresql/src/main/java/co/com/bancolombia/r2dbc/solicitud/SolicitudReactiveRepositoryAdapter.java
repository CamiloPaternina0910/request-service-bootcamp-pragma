package co.com.bancolombia.r2dbc.solicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.r2dbc.solicitud.entity.SolicitudEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class SolicitudReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        SolicitudEntity,
        String,
        SolicitudReactiveRepository
        > implements SolicitudRepository {

    public SolicitudReactiveRepositoryAdapter(SolicitudReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Solicitud.class));
    }

    @Override
    @Transactional
    public Mono<Solicitud> save(Solicitud entity) {
        log.info("Guardando solicitud");
        return super.save(entity)
                .doOnNext(solicitud -> log.info("Solicitud guardad {}", solicitud.getId()))
                .doOnError(e -> log.error("No se pudo guardar la solicitud por: {}", e.getMessage()));
    }

    @Override
    @Transactional
    public Mono<Solicitud> update(Solicitud solicitud) {
        log.info("Editando solicitud {}", solicitud.getId());
        return super.save(solicitud)
                .doOnNext(solicitudEditada -> log.info("Solicitud editada {}", solicitud.getId()))
                .doOnError(e -> log.error("No se pudo editar la solicitud por: {}", e.getMessage()));
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(String id) {
        log.info("Eliminando solicitud {}", id);
        return repository.deleteById(id)
                .doOnNext(solicitudEditada -> log.info("Solicitud eliminada {}", id))
                .doOnError(e -> log.error("No se pudo eliminar la solicitud por: {}", e.getMessage()));
    }
}
