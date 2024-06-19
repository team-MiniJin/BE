package com.minizin.travel.user.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.user.domain.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateEmailDto {
    @Getter
    @Setter
    public static class Request {

        @NotBlank
        @Email
        // 64 (local) + 1 (at) + 255 (domain) = 320
        @Size(max = 320, message = "Email must be less than 320 characters")
        private String email;

    }

    @Getter
    @Builder
    public static class Response {
        private Boolean success;
        private Long userId;
        private String changedEmail;

        public static Response fromUserEntity(UserEntity userEntity) {
            return Response.builder()
                    .success(true)
                    .userId(userEntity.getId())
                    .changedEmail(userEntity.getEmail())
                    .build();
        }
    }
}
