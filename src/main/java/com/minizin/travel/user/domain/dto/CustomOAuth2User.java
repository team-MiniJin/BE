package com.minizin.travel.user.domain.dto;

import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;

import java.util.Collection;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustomOAuth2User implements OAuth2User {

    private String username;
    private String nickname;
    private String email;
    private LoginType loginType;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return this.username;
    }

    public static UserEntity toUserEntity(CustomOAuth2User customOAuth2User) {
        return UserEntity.builder().username(customOAuth2User.getUsername())
                .email(customOAuth2User.getEmail()).nickname(customOAuth2User.getNickname())
                .loginType(customOAuth2User.getLoginType()).build();
    }

}
