package com.example.company.component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final String secretKey = "super_secret_key_12345678901234567890";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // Пропускаем запросы на /login и Swagger без проверки токена
        if (requestURI.equals("/login") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.equals("/swagger-ui.html")) {
            System.out.println("ФИЛЬТР НИЧЕГО НЕ БЛОКИРУЕТ");
            filterChain.doFilter(request, response);
            return;
        }

        // Получаем токен из заголовка Authorization
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String token = authorizationHeader.substring(7); // Убираем "Bearer "

        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(this.secretKey.getBytes());

            // Расшифровываем токен
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Извлекаем информацию из токена (например, email и роль)
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            // Создаем объект Authentication
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, getAuthorities(role));

            // Устанавливаем объект Authentication в SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // Если токен невалиден, возвращаем 401
            response.getWriter().write("Unauthorized: Missing or invalid token");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        // Пропускаем запрос дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }


    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        // Преобразуем роль в GrantedAuthority
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }
}

