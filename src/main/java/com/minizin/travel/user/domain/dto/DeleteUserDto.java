package com.minizin.travel.user.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.user.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeleteUserDto {

    @Getter
    @Builder
    public static class Response {
        private Boolean success;
        private Long userId;

        public static Response fromUserEntity(UserEntity userEntity) {
            return Response.builder()
                    .success(true)
                    .userId(userEntity.getId())
                    .build();
        }
    }
}
