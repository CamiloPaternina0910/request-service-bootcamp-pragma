package co.com.bancolombia.r2dbc.solicitud;

import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.r2dbc.solicitud.entity.SolicitudEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface SolicitudReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, String>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

    @Query("SELECT * FROM solicitud WHERE (:monto is NULL OR monto = :monto) AND (:plazo is NULL OR plazo = :plazo) AND (:correoElectronico is NULL OR email = :correoElectronico) AND (:idEstado is NULL OR id_estado = :idEstado) AND (:idTipoPrestamo is NULL OR  id_tipo_prestamo = :idTipoPrestamo)")
    Flux<Solicitud> findAll(
            @Param("monto") BigInteger monto,
            @Param("plazo") Integer plazo,
            @Param("correoElectronico") String correoElectronico,
            @Param("idEstado") String idEstado,
            @Param("idTipoPrestamo") String idTipoPrestamo,
            Pageable pageable
    );

    @Query("SELECT COUNT(*) FROM solicitud WHERE (:monto is NULL OR monto = :monto) AND (:plazo is NULL OR plazo = :plazo) AND (:correoElectronico is NULL OR email = :correoElectronico) AND (:idEstado is NULL OR id_estado = :idEstado) AND (:idTipoPrestamo is NULL OR  id_tipo_prestamo = :idTipoPrestamo)")
    Mono<Long> countByFiltros(
            @Param("monto") BigInteger monto,
            @Param("plazo") Integer plazo,
            @Param("correoElectronico") String correoElectronico,
            @Param("idEstado") String idEstado,
            @Param("idTipoPrestamo") String idTipoPrestamo
    );
}
