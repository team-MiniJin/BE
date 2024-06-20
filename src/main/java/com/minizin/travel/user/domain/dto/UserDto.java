package com.minizin.travel.user.domain.dto;

import com.minizin.travel.user.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

public class UserDto {
    @Getter
    @Builder
    public static class Response {
        private Long userId;
        private String username;
        private String email;
        private String nickname;

        public static Response fromUserEntity(UserEntity userEntity) {
            return Response.builder()
                    .userId(userEntity.getId())
                    .username(userEntity.getUsername())
                    .email(userEntity.getEmail())
                    .nickname(userEntity.getNickname())
                    .build();
        }
    }
}
