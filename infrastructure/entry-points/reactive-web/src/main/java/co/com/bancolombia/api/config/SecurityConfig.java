package co.com.bancolombia.api.config;

import co.com.bancolombia.api.security.BearerTokenServerAuthenticationConverter;
import co.com.bancolombia.api.security.JwtReactiveAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.secret:aa2d5cdbf2749b9b0bcf1c144b5b3d11e06a87d467f9490303f5bdc8f4572cd27e5f04db4b10ce371c7de84ddf75cd0c3bc0f2f97e540c4079019bb9d216cadb}")
    private String secret;

    private final String PATH_SOLICITUD = "/api/v1/solicitudes";

    private final String ROL_CLIENTE = "CLIENTE";
    private final String ROL_ASESOR = "ASESOR";
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter jwtAuthFilter = new AuthenticationWebFilter(
                new JwtReactiveAuthenticationManager(secret)
        );
        jwtAuthFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter());

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, PATH_SOLICITUD).hasAuthority(ROL_CLIENTE)
                        .pathMatchers(HttpMethod.GET, PATH_SOLICITUD).hasAuthority(ROL_ASESOR)
                        .anyExchange().permitAll()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, ex) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            byte[] bytes = "{\"message\" : \"Token invalido o ausente\" }".getBytes(StandardCharsets.UTF_8);
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                            return exchange.getResponse()
                                    .writeWith(Mono.just(exchange.getResponse()
                                            .bufferFactory().wrap(bytes)));
                        })
                        .accessDeniedHandler((exchange, denied) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            byte[] bytes = "{\"message\" : \"No tienes permisos\" }".getBytes(StandardCharsets.UTF_8);
                            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                            return exchange.getResponse()
                                    .writeWith(Mono.just(exchange.getResponse()
                                            .bufferFactory().wrap(bytes)));
                        })
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

}