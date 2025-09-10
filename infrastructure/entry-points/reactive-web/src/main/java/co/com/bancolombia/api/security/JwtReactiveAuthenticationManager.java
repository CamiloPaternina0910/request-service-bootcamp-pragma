package co.com.bancolombia.api.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.List;

public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final String jwtSecret;

    public JwtReactiveAuthenticationManager(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .filter(auth -> auth.getCredentials() != null)
                .map(auth -> {
                    String token = auth.getCredentials().toString();

                    Claims claims = Jwts.parser()
                            .verifyWith(getSecretKey())
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();

                    String documentoIdentificacion = claims.get("documentoIdentificacion", String.class);
                    @SuppressWarnings("unchecked")
                    List<String> roles = claims.get("authorization", List.class);

                    return (Authentication) new UsernamePasswordAuthenticationToken(
                            documentoIdentificacion,
                            null,
                            roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList()
                    );
                })
                .onErrorResume(e -> Mono.error(new AuthenticationException("Invalid JWT token") {}));
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
