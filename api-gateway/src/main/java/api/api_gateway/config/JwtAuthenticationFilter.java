package api.api_gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter {

    private final GatewayJwtUtil jwtUtil;

    public JwtAuthenticationFilter(GatewayJwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) 
            return unauthorized(exchange);

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) 
            return unauthorized(exchange);

        try {
            String email = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            // Downstream servise header ekleme
            exchange = exchange.mutate()
                    .request(r -> r.headers(h -> {
                        h.add("X-User-Email", email);
                        h.add("X-User-Role", role);
                    }))
                    .build();

        } catch (Exception e) {
            return unauthorized(exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
