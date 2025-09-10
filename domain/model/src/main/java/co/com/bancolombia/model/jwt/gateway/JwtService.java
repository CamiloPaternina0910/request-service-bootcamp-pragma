package co.com.bancolombia.model.jwt.gateway;

import reactor.core.publisher.Mono;

public interface JwtService {

    public Mono<String> getPrincipal();

}
