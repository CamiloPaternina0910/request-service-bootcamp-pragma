package co.com.bancolombia.webclient;

import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.exception.UsuarioNoEncontradoException;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UsuarioReactiveWebClientRepositoryAdapter implements UsuarioRepository {

    private WebClient webClient;

    private String PATH_USUARIO;

    public UsuarioReactiveWebClientRepositoryAdapter(
            @Value("${microservices.auth:http://localhost:8080/api/v1/usuarios}") String pathUsuario,
            WebClient.Builder builder
    ) {
        this.PATH_USUARIO = pathUsuario;
        this.webClient = builder.baseUrl(pathUsuario).build();
    }
    @Override
    public Mono<Usuario> findByDocumentoIdentificacion(String documentoIdentificacion) {
        log.info("Buscando usuario con documento de identidad: {}", documentoIdentificacion);
        return webClient.get()
                .uri(PATH_USUARIO + "/documentoIdentificacion/{documentoIdentificacion}", documentoIdentificacion)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(Usuario.class);
                    } else {
                        return Mono.error(new UsuarioNoEncontradoException(documentoIdentificacion));
                    }
                })
                .doOnNext(usuario -> log.info("Usuario encontrado {}", usuario.getCorreoElectronico()))
                .doOnError(e -> log.error("No se encontró el usuario buscado por el siguiente error: {}", e.getMessage()));
    }
}
