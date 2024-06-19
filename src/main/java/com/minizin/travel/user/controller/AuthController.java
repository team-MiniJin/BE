package com.minizin.travel.user.controller;

import com.minizin.travel.user.domain.dto.JoinDto;
import com.minizin.travel.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/jwt")
    public ResponseEntity<?> getJwt(HttpServletRequest request, HttpServletResponse response) {
        log.error("/auth/jwt start");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            log.error("there is cookie");
            for (Cookie cookie : cookies) {
                log.error("there is cookie : " + cookie.getName());
                if (cookie.getName().equals("Authorization")) {
                    log.error("there is Authorization cookie");
                    String token = cookie.getValue();
                    response.setHeader("Authorization", "Bearer " + token);
                    return ResponseEntity.ok("JWT 헤더로 발급");
                }
            }
        }
        log.error("cookie is null");
        return ResponseEntity.status(401).body("Unauthorized");
    }

    @PostMapping("/auth/join")
    public JoinDto.Response join(
            @RequestBody @Valid JoinDto.Request request
    ) {
        return authService.join(request);
    }
}
