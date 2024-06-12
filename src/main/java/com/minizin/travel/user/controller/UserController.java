package com.minizin.travel.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.minizin.travel.user.dto.UserDto;
import com.minizin.travel.user.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Class: BaseEntity Project: com.minizin.travel.user.service.UserService
 * <p>
 * Description: UserController
 *
 * @author JANG CHIHUN
 * @date 6/3/24 21:30 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        UserDto user = userService.register(userDto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user{user_id}")
    public ResponseEntity<UserDto> findUser(@PathVariable("user_id") Long userid) {
        UserDto user = userService.findUser(userid);
        return ResponseEntity.ok(user);
    }

    @PutMapping("user/{user_id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("user_id") Long userId, @RequestBody UserDto userDto) {
        UserDto user = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable("user_id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("회원 탈퇴가 성공적으로 완료했습니다.");
    }
}
