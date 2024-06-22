package com.minizin.travel.user.oauth2;

import com.minizin.travel.user.domain.dto.PrincipalDetails;
import com.minizin.travel.user.domain.enums.Role;
import com.minizin.travel.user.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String username = principalDetails.getUsername();
        Role role = principalDetails.getUserEntity().getRole();

        String token = tokenProvider.createJwt(username, role.toString(), 24 * 60 * 60 * 1000L);

        // 응답에 쿠키로 jwt 발급
        response.addHeader("Set-Cookie", createCookie("Authorization", token).toString());

        // 프론트 측 특정 uri로 리다이렉트
        response.sendRedirect("https://fe-two-blond.vercel.app/kakao-redirect");
    }

    private ResponseCookie createCookie(String key, String value) {

        ResponseCookie cookie = ResponseCookie.from(key, value)
                .maxAge(60 * 60 * 60)
                .secure(true)
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .build();

        return cookie;
    }

}
