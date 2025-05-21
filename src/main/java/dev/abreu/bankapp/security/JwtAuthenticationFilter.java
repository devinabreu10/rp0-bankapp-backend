package dev.abreu.bankapp.security;

import dev.abreu.bankapp.entity.Customer;
import dev.abreu.bankapp.service.CustomerService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${application.cors.origins}")
    private String[] allowedOrigins;

    private final JwtConfig jwtConfig;
    private final CustomerService customerService;

    public JwtAuthenticationFilter(JwtConfig jwtConfig, CustomerService customerService) {
        this.jwtConfig = jwtConfig;
        this.customerService = customerService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String username;

        try {
            //if header is empty or doesn't include 'Bearer' then return
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            username = jwtConfig.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Customer customer = customerService.getCustomerByUsername(username);
                if (jwtConfig.isTokenValid(jwt, customer)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            customer,
                            null,
                            customer.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            if (StringUtils.isNotBlank(allowedOrigins[0])) {
                response.setHeader("Access-Control-Allow-Origin", allowedOrigins[0]);
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Access token expired, please login again.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
