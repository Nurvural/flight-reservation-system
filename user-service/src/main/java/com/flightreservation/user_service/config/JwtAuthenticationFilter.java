package com.flightreservation.user_service.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.flightreservation.user_service.services.CustomUserDetailsService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws java.io.IOException, jakarta.servlet.ServletException {

    	//Her request’te bir kere çalışan filter, header’dan token alır
        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            email = jwtUtil.extractUsername(token); // email alınır
        }

        // Eğer email varsa ve SecurityContext boşsa (yani kullanıcı henüz auth edilmedi)
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	// DB’den user çek
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            // Token geçerli mi kontrol ediliyor
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
            	//Spring Security için AuthenticationToken oluşturuluyor
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //SecurityContext’e kullanıcı bilgisi yazılıyor
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
