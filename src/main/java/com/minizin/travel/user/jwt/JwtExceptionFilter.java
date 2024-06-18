package com.minizin.travel.user.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minizin.travel.user.domain.dto.ErrorResponse;
import com.minizin.travel.user.domain.enums.JwtErrorCode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            this.setErrorResponse(response, e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, Throwable e) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        ErrorResponse errorResponse = new ErrorResponse(
                JwtErrorCode.INVALID_JWT.getStatus(), JwtErrorCode.INVALID_JWT.getMessage()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(result);
    }
}
