package co.com.bancolombia.config;

import co.com.bancolombia.model.estado.gateways.EstadoRepository;
import co.com.bancolombia.model.tipoprestamo.gateways.TipoPrestamoRepository;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import co.com.bancolombia.usecase.solicitud.validation.SolicitudValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SolicitudUseCaseConfig {

    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;
    private final UsuarioRepository usuarioRepository;

    @Bean
    public SolicitudValidator solicitudValidator(){
        return new SolicitudValidator(tipoPrestamoRepository, estadoRepository, usuarioRepository);
    }
}
