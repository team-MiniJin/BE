package com.minizin.travel.user.oauth2.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/cookie-to-header")
    public ResponseEntity<?> convert(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization")) {
                    String token = cookie.getValue();
                    response.setHeader("Authorization", token);
                    return ResponseEntity.ok("JWT 헤더로 발급");
                }
            }
        }
        return ResponseEntity.status(401).body("Unauthorized");
    }
}
