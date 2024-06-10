package com.minizin.travel.user.domain.dto;

import java.util.Map;

public class KakaoResponse {

    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccount;
    private final Map<String, Object> profile;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.profile = (Map<String, Object>) kakaoAccount.get("profile");
    }

    // 제공자 (Ex. naver, google, ...)
    public String getProvider() {
        return "kakao";
    }

    // 제공자에서 발급해주는 아이디(번호)
    public String getProviderId() {
        return this.attributes.get("id").toString();
    }

    // 이메일
    public String getEmail() {
        return this.kakaoAccount.get("email").toString();
    }

    // 프로필로 설정된 이름
    public String getNickname() {
        return this.profile.get("nickname").toString();
    }
}
