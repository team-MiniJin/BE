package com.minizin.travel.user.controller;

import com.minizin.travel.user.domain.dto.FindIdDto;
import com.minizin.travel.user.domain.dto.FindPasswordDto;
import com.minizin.travel.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

}