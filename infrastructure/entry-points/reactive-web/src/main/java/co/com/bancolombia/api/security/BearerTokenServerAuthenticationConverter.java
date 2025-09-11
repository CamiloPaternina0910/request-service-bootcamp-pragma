package co.com.bancolombia.api.security;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class BearerTokenServerAuthenticationConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest())
                .map(request -> request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader != null && authHeader.startsWith("Bearer "))
                .map(authHeader -> authHeader.substring(7))
                .map(token -> new BearerTokenAuthentication(token));
    }

    private static class BearerTokenAuthentication implements Authentication {
        private final String token;

        public BearerTokenAuthentication(String token) {
            this.token = token;
        }

        @Override
        public String getCredentials() {
            return token;
        }

        @Override
        public Object getPrincipal() {
            return token;
        }

        @Override
        public boolean isAuthenticated() {
            return false;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        }

        @Override
        public String getName() {
            return token;
        }

        @Override
        public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
            return java.util.Collections.emptyList();
        }

        @Override
        public Object getDetails() {
            return null;
        }
    }
}