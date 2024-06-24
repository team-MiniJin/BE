package com.minizin.travel.user.dto;

import com.minizin.travel.user.domain.User;

/**
 * Class: BaseEntity Project: com.minizin.travel.user.dto
 * <p>
 * Description: UserDto
 *
 * @author JANG CHIHUN
 * @date 6/3/24 21:30 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
public record UserDto(String username, String password, String nickname, String email, String phone) {

    public static User toEntity(UserDto userDto) {
        return User.builder()
                .username(userDto.username())
                .password(userDto.password())
                .nickname(userDto.nickname())
                .email(userDto.email())
                .phone(userDto.phone())
                .build();
    }

    public static UserDto of(User user) {
        return new UserDto(
            user.getUsername(),
            user.getPassword(),
            user.getNickname(),
            user.getEmail(),
            user.getPhone()
        );
    }
}
