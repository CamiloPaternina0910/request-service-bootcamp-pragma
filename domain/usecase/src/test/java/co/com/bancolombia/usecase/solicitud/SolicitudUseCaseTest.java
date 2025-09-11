package co.com.bancolombia.usecase.solicitud;

import co.com.bancolombia.model.jwt.gateway.JwtService;
import co.com.bancolombia.model.solicitud.Solicitud;
import co.com.bancolombia.model.solicitud.gateways.SolicitudRepository;
import co.com.bancolombia.model.solicitud.exception.EstadoNoEncontradoException;
import co.com.bancolombia.model.solicitud.exception.TipoPrestamoNoEncontradoException;
import co.com.bancolombia.model.usuario.exception.UsuarioNoEncontradoException;
import co.com.bancolombia.usecase.solicitud.validation.SolicitudValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudUseCaseTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private SolicitudValidator solicitudValidator;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private SolicitudUseCase solicitudUseCase;

    private Solicitud solicitud;
    private Solicitud solicitudValidada;
    private Solicitud solicitudGuardada;

    @BeforeEach
    void setUp() {
        solicitud = Solicitud.builder()
                .documentoIdentificacion("123456789")
                .idTipoPrestamo("1")
                .correoElectronico("test@test.com")
                .plazo(12)
                .monto(new BigInteger("1000000"))
                .build();

        solicitudValidada = Solicitud.builder()
                .documentoIdentificacion("123456789")
                .idTipoPrestamo(UUID.randomUUID().toString())
                .idEstado(UUID.randomUUID().toString())
                .correoElectronico("test@test.com")
                .plazo(12)
                .monto(new BigInteger("1000000"))
                .build();


        solicitudGuardada = Solicitud.builder()
                .id(UUID.randomUUID().toString())
                .documentoIdentificacion("123456789")
                .idTipoPrestamo(solicitudValidada.getIdTipoPrestamo())
                .idEstado(solicitudValidada.getIdEstado())
                .correoElectronico("test@test.com")
                .plazo(12)
                .monto(new BigInteger("1000000"))
                .build();

        when(jwtService.getPrincipal()).thenReturn(Mono.just("123456789"));
    }

    @Test
    void save_WhenValidSolicitud_ShouldSaveSuccessfully() {
        when(solicitudValidator.validarCreacionSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.just(solicitudValidada));

        when(solicitudRepository.save(any(Solicitud.class)))
                .thenReturn(Mono.just(solicitudGuardada));

        StepVerifier.create(solicitudUseCase.save(solicitud))
                .expectNext(solicitudGuardada)
                .verifyComplete();

        verify(solicitudValidator).validarCreacionSolicitud(solicitud);
        verify(solicitudRepository).save(solicitudValidada);
    }

    @Test
    void save_WhenUsuarioNoEncontrado_ShouldPropagateUsuarioNoEncontradoException() {
        String documento = "123456789";
        UsuarioNoEncontradoException exception = new UsuarioNoEncontradoException(documento);

        when(solicitudValidator.validarCreacionSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.error(exception));

        StepVerifier.create(solicitudUseCase.save(solicitud))
                .expectErrorMatches(throwable ->
                        throwable instanceof UsuarioNoEncontradoException &&
                                throwable.getMessage().contains(documento))
                .verify();

        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void save_WhenTipoPrestamoNoEncontrado_ShouldPropagateTipoPrestamoNoEncontradoException() {
        String idTipoPrestamo = "1";
        TipoPrestamoNoEncontradoException exception = new TipoPrestamoNoEncontradoException(idTipoPrestamo);

        when(solicitudValidator.validarCreacionSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.error(exception));

        StepVerifier.create(solicitudUseCase.save(solicitud))
                .expectErrorMatches(throwable ->
                        throwable instanceof TipoPrestamoNoEncontradoException &&
                                throwable.getMessage().contains(idTipoPrestamo))
                .verify();

        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void save_WhenEstadoNoEncontrado_ShouldPropagateEstadoNoEncontradoException() {
        String estadoNombre = "Pendiente de revisión";
        EstadoNoEncontradoException exception = new EstadoNoEncontradoException(estadoNombre);

        when(solicitudValidator.validarCreacionSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.error(exception));

        StepVerifier.create(solicitudUseCase.save(solicitud))
                .expectErrorMatches(throwable ->
                        throwable instanceof EstadoNoEncontradoException &&
                                throwable.getMessage().contains(estadoNombre))
                .verify();

        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void save_ShouldPreserveAllSolicitudData() {
        Solicitud solicitudCompleta = Solicitud.builder()
                .documentoIdentificacion("987654321")
                .idTipoPrestamo("2")
                .correoElectronico("user@domain.com")
                .plazo(24)
                .monto(new BigInteger("5000000"))
                .build();

        Solicitud solicitudValidadaCompleta = Solicitud.builder()
                .documentoIdentificacion("987654321")
                .idTipoPrestamo(UUID.randomUUID().toString())
                .idEstado(UUID.randomUUID().toString())
                .correoElectronico("user@domain.com")
                .plazo(24)
                .monto(new BigInteger("5000000"))
                .build();

        Solicitud solicitudGuardadaCompleta = Solicitud.builder()
                .id(UUID.randomUUID().toString())
                .documentoIdentificacion("987654321")
                .idTipoPrestamo(solicitudValidadaCompleta.getIdTipoPrestamo())
                .idEstado(solicitudValidadaCompleta.getIdEstado())
                .correoElectronico("user@domain.com")
                .plazo(24)
                .monto(new BigInteger("5000000"))
                .build();

        when(solicitudValidator.validarCreacionSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.just(solicitudValidadaCompleta));

        when(solicitudRepository.save(any(Solicitud.class)))
                .thenReturn(Mono.just(solicitudGuardadaCompleta));

        StepVerifier.create(solicitudUseCase.save(solicitudCompleta))
                .expectNextMatches(saved ->
                        saved.getDocumentoIdentificacion().equals("987654321") &&
                                saved.getIdTipoPrestamo().equals(solicitudValidadaCompleta.getIdTipoPrestamo()) &&
                                saved.getIdEstado().equals(solicitudValidadaCompleta.getIdEstado()) &&
                                saved.getCorreoElectronico().equals("user@domain.com") &&
                                saved.getPlazo().equals(24) &&
                                saved.getMonto().equals(new BigInteger("5000000")))
                .verifyComplete();
    }

    @Test
    void findAllSolicitudes() {
        when(solicitudRepository.findAll(null, 0, 1)).thenReturn(Flux.just(solicitudGuardada));

        StepVerifier.create(solicitudUseCase.findAll(null, 0, 1))
                .expectNext(solicitudGuardada)
                .verifyComplete();
    }
}