package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.CustomOAuth2User;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DefaultOAuth2UserService delegate;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Test
    @DisplayName("소셜 로그인 시 사용자 정보 획득 성공")
    void loadUser_success() {
        //given
        OAuth2User oAuth2User = mock(OAuth2User.class);
        given(delegate.loadUser(any())).willReturn(oAuth2User);

        OAuth2UserRequest userRequest = mock(OAuth2UserRequest.class);
        ClientRegistration clientRegistration = mock(ClientRegistration.class);
        when(userRequest.getClientRegistration()).thenReturn(clientRegistration);
        when(clientRegistration.getRegistrationId()).thenReturn("kakao");

        Map<String, Object> attributes = Map.of(
                "id", "12345",
                "kakao_account", Map.of(
                        "email", "test@example.com",
                        "profile", Map.of("nickname", "Test User")
                )
        );
        when(oAuth2User.getAttributes()).thenReturn(attributes);
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.empty());

        //when
        OAuth2User oAuth2User1 = customOAuth2UserService.loadUser(userRequest);

        //then
        assertNotNull(oAuth2User1);
        assertEquals("kakao 12345", ((CustomOAuth2User) oAuth2User1).getUsername());
        assertEquals("test@example.com", ((CustomOAuth2User) oAuth2User1).getEmail());
        assertEquals("Test User", ((CustomOAuth2User) oAuth2User1).getNickname());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

}