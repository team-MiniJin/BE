package com.minizin.travel.oauth2;

import com.minizin.travel.jwt.JwtUtil;
import com.minizin.travel.user.domain.dto.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        String token = jwtUtil.createJwt(username, 60 * 60 * 60L);

        // 응답에 쿠키로 jwt 발급
        response.addCookie(createCookie("Authorization", token));

        // 프론트 측 특정 uri로 리다이렉트
        response.sendRedirect("http://localhost:3000/");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        // 쿠키 지속 시간
        cookie.setMaxAge(60 * 60 * 60);

        // https 통신에서만 쿠키 사용 (현재는 비활성화)
        //cookie.setSecure(true);

        // 쿠키 전역에서 볼 수 있도록 설정
        cookie.setPath("/");

        // js 접근 방지
        cookie.setHttpOnly(true);

        return cookie;
    }

}
