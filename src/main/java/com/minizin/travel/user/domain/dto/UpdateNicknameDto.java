package com.minizin.travel.user.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.user.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateNicknameDto {
    @Getter
    @Setter
    public static class Request {

        private String nickname;

    }

    @Getter
    @Builder
    public static class Response {
        private Boolean success;
        private Long userId;
        private String changedNickname;

        public static Response fromUserEntity(UserEntity userEntity) {
            return Response.builder()
                    .success(true)
                    .userId(userEntity.getId())
                    .changedNickname(userEntity.getNickname())
                    .build();
        }
    }
}
