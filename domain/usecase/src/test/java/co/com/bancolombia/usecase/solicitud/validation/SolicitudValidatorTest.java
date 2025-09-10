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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudValidatorTest {

    @Mock
    private TipoPrestamoRepository tipoPrestamoRepository;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SolicitudValidator solicitudValidator;

    private Solicitud solicitud;

    private final String ESTADO_INICIAL = "PENDIENTE";

    private Estado estadoInicial;

    private TipoPrestamo tipoPrestamo;

    @BeforeEach
    void setUp() {
        estadoInicial = Estado.builder()
                .id(UUID.randomUUID().toString())
                .nombre(ESTADO_INICIAL)
                .build();

        tipoPrestamo = TipoPrestamo.builder()
                .id(UUID.randomUUID().toString())
                .build();

        solicitud = Solicitud.builder()
                .documentoIdentificacion("123456789")
                .idTipoPrestamo(tipoPrestamo.getId())
                .correoElectronico("test@test.com")
                .plazo(12)
                .monto(new BigInteger("1000000"))
                .build();
    }

    @Test
    void validarCreacionSolicitud_WhenAllValid_ShouldReturnSolicitudWithEstado() {
        Usuario usuario = new Usuario();
        usuario.setDocumentoIdentificacion("123456789");
        usuario.setCorreoElectronico("test@test.com");

        when(usuarioRepository.findByDocumentoIdentificacion("123456789"))
                .thenReturn(Mono.just(usuario));
        when(tipoPrestamoRepository.findById(tipoPrestamo.getId()))
                .thenReturn(Mono.just(tipoPrestamo));
        when(estadoRepository.findByNombre(ESTADO_INICIAL))
                .thenReturn(Mono.just(estadoInicial));

        StepVerifier.create(solicitudValidator.validarCreacionSolicitud(solicitud))
                .expectNextMatches(validatedSolicitud ->
                        validatedSolicitud.getDocumentoIdentificacion().equals("123456789") &&
                                validatedSolicitud.getIdTipoPrestamo().equals(tipoPrestamo.getId()) &&
                                validatedSolicitud.getIdEstado().equals(estadoInicial.getId()) &&
                                validatedSolicitud.getCorreoElectronico().equals("test@test.com") &&
                                validatedSolicitud.getPlazo().equals(12) &&
                                validatedSolicitud.getMonto().equals(new BigDecimal("1000000")))
                .verifyComplete();
    }

    @Test
    void validarCreacionSolicitud_WhenUsuarioNotFound_ShouldThrowException() {
        when(usuarioRepository.findByDocumentoIdentificacion("123456789"))
                .thenReturn(Mono.empty());

        when(estadoRepository.findByNombre(ESTADO_INICIAL)).thenReturn(Mono.just(estadoInicial));
        when(tipoPrestamoRepository.findById(tipoPrestamo.getId())).thenReturn(Mono.just(tipoPrestamo));

        StepVerifier.create(solicitudValidator.validarCreacionSolicitud(solicitud))
                .expectError(UsuarioNoEncontradoException.class)
                .verify();
    }

    @Test
    void validarCreacionSolicitud_WhenTipoPrestamoNotFound_ShouldThrowException() {
        Usuario usuario = new Usuario();
        usuario.setDocumentoIdentificacion("123456789");

        when(usuarioRepository.findByDocumentoIdentificacion("123456789"))
                .thenReturn(Mono.just(usuario));
        when(tipoPrestamoRepository.findById("1"))
                .thenReturn(Mono.empty());
        when(estadoRepository.findByNombre(ESTADO_INICIAL))
                .thenReturn(Mono.just(estadoInicial));

        solicitud.setIdTipoPrestamo("1");
        StepVerifier.create(solicitudValidator.validarCreacionSolicitud(solicitud))
                .expectError(TipoPrestamoNoEncontradoException.class)
                .verify();
    }

    @Test
    void validarCreacionSolicitud_WhenEstadoNotFound_ShouldThrowException() {
        Usuario usuario = new Usuario();
        usuario.setDocumentoIdentificacion("123456789");

        when(usuarioRepository.findByDocumentoIdentificacion("123456789"))
                .thenReturn(Mono.just(usuario));
        when(tipoPrestamoRepository.findById(tipoPrestamo.getId()))
                .thenReturn(Mono.just(tipoPrestamo));
        when(estadoRepository.findByNombre(ESTADO_INICIAL))
                .thenReturn(Mono.empty());

        StepVerifier.create(solicitudValidator.validarCreacionSolicitud(solicitud))
                .expectError(EstadoNoEncontradoException.class)
                .verify();
    }
}