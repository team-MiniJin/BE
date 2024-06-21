package com.minizin.travel.user.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 특정 경로 무시
        String requestUri = request.getRequestURI();
        if (requestUri.matches("^\\/login(?:\\/.*)?$") ||
                requestUri.matches("^\\/oauth2(?:\\/.*)?$") ||
                requestUri.matches("^\\/auth(?:\\/.*)?$") ||
                requestUri.matches("^\\/mails\\/auth-code(?:\\/.*)?$") ||
                requestUri.matches("^\\/users\\/find-id(?:\\/.*)?$") ||
                requestUri.matches("^\\/users\\/find-password(?:\\/.*)?$") ||
                requestUri.matches("^\\/plans\\/others(?:\\/.*)?$") ||
                requestUri.matches("^\\/plans\\/popular\\/week(?:\\/.*)?$")
        ) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveTokenFromRequest(request);

        // 토큰이 유효하다면
        if (StringUtils.hasText(token) && !tokenProvider.isExpired(token)) {

            // 스프링 시큐리티 인증 토큰 생성
            Authentication authToken = tokenProvider.getAuthentication(token);

            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
