package co.com.bancolombia.webclient;

import co.com.bancolombia.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioReactiveWebClientRepository {

    Mono<Usuario> findByDocumentoIdentificacion(String documentoIdentificacion);

}
