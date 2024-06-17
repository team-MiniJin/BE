package com.minizin.travel.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class FindPasswordDto {
    @Getter
    @Setter
    public static class Request {

        @NotBlank
        @Size(min = 6, max = 20, message = "Username must be between 6 and 20 characters")
        private String username;

        @NotBlank
        @Email
        // 64 (local) + 1 (at) + 255 (domain) = 320
        @Size(max = 320, message = "Email must be less than 320 characters")
        String email;
    }

}
