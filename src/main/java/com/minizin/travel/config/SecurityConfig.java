package com.minizin.travel.config;

import com.minizin.travel.user.auth.LoginFilter;
import com.minizin.travel.user.jwt.JwtAuthenticationFilter;
import com.minizin.travel.user.jwt.TokenProvider;
import com.minizin.travel.user.oauth2.CustomSuccessHandler;
import com.minizin.travel.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final TokenProvider tokenProvider;

    // BCryptPasswordEncoder Bean 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        log.debug("Creating BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager Bean 등록
    @Bean
    @Lazy
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        log.debug("Creating AuthenticationManager bean");
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("Configuring SecurityFilterChain");
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(
                        (request -> {
                            CorsConfiguration configuration = new CorsConfiguration();
                            log.debug("Setting CORS configuration");

                            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                            configuration.setAllowedMethods(Collections.singletonList("*"));
                            configuration.setAllowCredentials(true);
                            configuration.setAllowedHeaders(Collections.singletonList("*"));
                            configuration.setMaxAge(3600L);

                            configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                            configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                            return configuration;
                        })
                ));

        //csrf disable
        http
            .csrf((csrf) -> {
                log.debug("Disabling CSRF");
                csrf.disable();
            });

        //From 로그인 방식 disable
        http
            .formLogin((formLogin) -> {
                log.debug("Disabling formLogin");
                formLogin.disable();
            });

        //HTTP Basic 인증 방식 disable
        http
            .httpBasic((httpBasic) -> {
                log.debug("Disabling httpBasic");
                httpBasic.disable();
            });


        //oauth2
        http
            .oauth2Login((oauth2) -> {
                log.debug("Configuring OAuth2 login");
                oauth2.userInfoEndpoint((userInfoEndpointConfig) ->
                        userInfoEndpointConfig.userService(customOAuth2UserService))
                    .successHandler(customSuccessHandler);
            });

        //경로별 인가 작업
        http
            .authorizeHttpRequests((auth) -> {
                log.debug("Configuring URL authorization");
                auth.requestMatchers("/", "/auth/join", "/auth/login", "/auth/jwt", "/mails/auth-code","/tour/*").permitAll()
                    .anyRequest().authenticated();
            });

        // UsernamePasswordAuthenticationFilter 자리에 LoginFilter 추가
        http
                .addFilterAt(new LoginFilter(
                        authenticationManager(authenticationConfiguration), tokenProvider
                ), UsernamePasswordAuthenticationFilter.class);

        //JWT Authentication Filter 추가
        http
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider),
                        LoginFilter.class);

        //세션 설정 : STATELESS
        http
            .sessionManagement((session) -> {
                log.debug("Setting session management to stateless");
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            });

        return http.build();
    }

}