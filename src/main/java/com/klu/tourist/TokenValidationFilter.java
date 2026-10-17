
package com.klu.tourist;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenValidationFilter
        extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public TokenValidationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest request) {

        return !("DELETE".equalsIgnoreCase(
                    request.getMethod())
                && request.getServletPath()
                    .startsWith("/api/tourists/"));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header =
                request.getHeader("Authorization");

        if (header == null
                || !header.startsWith("Bearer ")
                || !jwtUtil.validateToken(
                        header.substring(7))) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"Invalid or expired JWT\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
