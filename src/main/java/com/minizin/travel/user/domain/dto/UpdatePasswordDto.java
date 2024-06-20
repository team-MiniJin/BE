package com.minizin.travel.user.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.user.domain.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdatePasswordDto {
    @Getter
    @Setter
    public static class Request {

        @NotBlank
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!-~]).*$",
                message = "Password must contain at least one uppercase letter," +
                        " one lowercase letter, one number, and one special character")
        private String originalPassword;

        @NotBlank
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!-~]).*$",
                message = "Password must contain at least one uppercase letter," +
                        " one lowercase letter, one number, and one special character")
        private String changePassword;

    }

    @Getter
    @Builder
    public static class Response {
        private Boolean success;
        private Long userId;
        private String changedPassword;

        public static Response fromUserEntity(UserEntity userEntity) {
            return Response.builder()
                    .success(true)
                    .userId(userEntity.getId())
                    .changedPassword(userEntity.getPassword())
                    .build();
        }
    }
}
