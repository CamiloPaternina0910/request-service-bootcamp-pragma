package co.com.bancolombia.webclient;

import co.com.bancolombia.model.usuario.Usuario;
import co.com.bancolombia.model.usuario.gateways.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UsuarioReactiveWebClientRepositoryAdapter implements UsuarioRepository {

    private WebClient webClient;

    private final String PATH_USUARIO = "http://localhost:8080/api/v1/usuarios/documentoIdentificacion";

    public UsuarioReactiveWebClientRepositoryAdapter(){
        this.webClient = WebClient.builder().baseUrl(PATH_USUARIO).build();
    }

    @Override
    public Mono<Usuario> findByDocumentoIdentificacion(String documentoIdentificacion) {
        log.info("Buscando usuario con documento de identidad: {}", documentoIdentificacion);
        return webClient.get()
                .uri("/{documentoIdentificacion}", documentoIdentificacion)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(Usuario.class);
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                })
                .doOnNext(usuario -> log.info("Usuario encontrado {}", usuario.getCorreoElectronico()))
                .doOnError(e -> log.error("No se encontró el usuario buscado por el siguiente error: {}", e.getMessage()));
    }
}
