package com.minizin.travel.user.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minizin.travel.user.domain.dto.ErrorResponse;
import com.minizin.travel.user.domain.dto.PrincipalDetails;
import com.minizin.travel.user.domain.enums.Role;
import com.minizin.travel.user.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public LoginFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        // 로그인 path 설정
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth/login", "POST"));
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String username = principalDetails.getUsername();
        Role role = principalDetails.getUserEntity().getRole();

        String token = tokenProvider.createJwt(username, role.toString(), 24 * 60 * 60 * 1000L);

        response.addHeader("Authorization", "Bearer " + token);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(result);
    }
}
