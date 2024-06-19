package com.minizin.travel.user.controller;

import com.minizin.travel.user.domain.dto.*;
import com.minizin.travel.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/find-id")
    public FindIdDto.Response findId(
            @RequestBody @Valid FindIdDto.Request request
    ) {
        return userService.findId(request);
    }

    @PostMapping("/users/find-password")
    public String findPassword(
            @RequestBody @Valid FindPasswordDto.Request request
    ) {
        userService.findPassword(request);
        return "임시 비밀번호가 메일로 전송되었습니다.";
    }

    @PatchMapping("/users/password")
    public UpdatePasswordDto.Response updatePassword(
            @RequestBody @Valid UpdatePasswordDto.Request request,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return userService.updatePassword(request, principalDetails);
    }

    @PatchMapping("/users/email")
    public UpdateEmailDto.Response updateEmail(
            @RequestBody @Valid UpdateEmailDto.Request request,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return userService.updateEmail(request, principalDetails);
    }
}
