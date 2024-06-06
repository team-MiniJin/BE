package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.CustomOAuth2User;
import com.minizin.travel.user.domain.dto.KakaoResponse;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        KakaoResponse kakaoResponse = null;
        if ("kakao".equals(registrationId)) {
            kakaoResponse = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디 값을 만듬
        String username = kakaoResponse.getProvider() + " " + kakaoResponse.getProviderId();
        CustomOAuth2User customOAuth2User = CustomOAuth2User.builder()
                .username(username)
                .email(kakaoResponse.getEmail())
                .nickname(kakaoResponse.getNickname())
                .loginType(LoginType.KAKAO)
                .build();

        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            userRepository.save(CustomOAuth2User.toUserEntity(customOAuth2User));
        } else {
            user.get().updateNickname(customOAuth2User.getNickname());
        }

        return customOAuth2User;
    }
}
