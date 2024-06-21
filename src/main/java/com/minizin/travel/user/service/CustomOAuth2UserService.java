package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.KakaoResponse;
import com.minizin.travel.user.domain.dto.PrincipalDetails;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.enums.Role;
import com.minizin.travel.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final DefaultOAuth2UserService delegate;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        KakaoResponse kakaoResponse = null;
        if ("kakao".equals(registrationId)) {
            kakaoResponse = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디 값을 만듬
        String username = kakaoResponse.getProvider() + " " + kakaoResponse.getProviderId();

        UserEntity userEntity = null;
        Optional<UserEntity> existUser = userRepository.findByUsername(username);
        if (existUser.isEmpty()) {
            userEntity = userRepository.save(UserEntity.builder()
                    .username(username)
                    .email(kakaoResponse.getEmail())
                    .nickname(kakaoResponse.getNickname())
                    .role(Role.ROLE_USER)
                    .loginType(LoginType.KAKAO)
                    .build());
        } else {
            userEntity = existUser.get();
        }

        return new PrincipalDetails(userEntity);
    }
}
