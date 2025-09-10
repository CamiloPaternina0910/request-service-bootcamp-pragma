package co.com.bancolombia.jwt;

import co.com.bancolombia.model.jwt.gateway.JwtService;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
public class JwtServiceImp implements JwtService {

    @Override
    public Mono<String> getPrincipal() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no autenticado")));
    }

}
