package com.minizin.travel.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyAuthCodeDto {

    @NotBlank
    @Email
    // 64 (local) + 1 (at) + 255 (domain) = 320
    @Size(max = 320, message = "Email must be less than 320 characters")
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\d{6}$", message = "AuthCode must be 6 digits")
    private String authCode;

}
