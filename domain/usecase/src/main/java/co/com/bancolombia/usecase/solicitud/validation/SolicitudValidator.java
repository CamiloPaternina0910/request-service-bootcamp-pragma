package co.com.bancolombia.usecase.solicitud.validation;

import co.com.bancolombia.model.estado.Estado;
import co.com.bancolombia.model.estado.gateways.EstadoRepository;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.tipoprestamo.TipoPrestamo;
import co.com.bancolombia.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import co.com.bancolombia.model.solicitud.exception.EstadoNoEncontradoException;
import co.com.bancolombia.model.solicitud.exception.TipoPrestamoNoEncontradoException;
import co.com.bancolombia.model.usuario.exception.UsuarioNoEncontradoException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class SolicitudValidator {

    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioRepository usuarioRepository;

    private final String NOMBRE_ESTADO_INICIAL = "PENDIENTE";

    public Mono<Solicitud> validarCreacionSolicitud(Solicitud solicitud) {
        Mono<Usuario> usuarioMono = usuarioRepository.findByDocumentoIdentificacion(solicitud.getDocumentoIdentificacion());

        Mono<TipoPrestamo> tipoPrestamoMono = tipoPrestamoRepository.findById(solicitud.getIdTipoPrestamo())
                .switchIfEmpty(Mono.defer(() ->Mono.error(new TipoPrestamoNoEncontradoException(solicitud.getIdTipoPrestamo()))));

        Mono<Estado> estadoMono = estadoRepository.findByNombre(NOMBRE_ESTADO_INICIAL)
                .switchIfEmpty(Mono.defer(() ->Mono.error(new EstadoNoEncontradoException(NOMBRE_ESTADO_INICIAL))));

        return Mono.zip(usuarioMono, tipoPrestamoMono, estadoMono)
                .map(tuple -> {
                            Estado estado = tuple.getT3();
                            Usuario usuario = tuple.getT1();
                            return Solicitud.builder()
                                    .idTipoPrestamo(solicitud.getIdTipoPrestamo())
                                    .idEstado(estado.getId())
                                    .correoElectronico(usuario.getCorreoElectronico())
                                    .plazo(solicitud.getPlazo())
                                    .monto(solicitud.getMonto())
                                    .documentoIdentificacion(usuario.getDocumentoIdentificacion())
                                    .build();
                        }
                );
    }
}
