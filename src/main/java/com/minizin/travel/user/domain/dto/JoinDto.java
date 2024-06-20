package com.minizin.travel.user.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JoinDto {
    @Getter
    @Setter
    public static class Request {
        @NotBlank
        @Size(min = 6, max = 20, message = "Username must be between 6 and 20 characters")
        private String username;

        @NotBlank
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!-~]).*$",
                message = "Password must contain at least one uppercase letter," +
                        " one lowercase letter, one number, and one special character")
        private String password;

        private String nickname;

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
        private String username;
        private String password;
        private String nickname;
        private String email;
        private Role role;
        private LoginType loginType;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Response fromUserEntity(UserEntity userEntity) {
            return Response.builder()
                    .success(true)
                    .userId(userEntity.getId())
                    .username(userEntity.getUsername())
                    .password(userEntity.getPassword())
                    .nickname(userEntity.getNickname())
                    .email(userEntity.getEmail())
                    .role(userEntity.getRole())
                    .loginType(userEntity.getLoginType())
                    .createdAt(userEntity.getCreatedAt())
                    .updatedAt(userEntity.getUpdatedAt())
                    .build();
        }
    }
}
