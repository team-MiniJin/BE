package com.minizin.travel.user.service;

import static com.minizin.travel.user.exception.UserErrorCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minizin.travel.global.exception.CustomException;
import com.minizin.travel.user.domain.User;
import com.minizin.travel.user.dto.UserDto;
import com.minizin.travel.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Class: BaseEntity Project: com.minizin.travel.user.service
 * <p>
 * Description: UserService
 *
 * @author JANG CHIHUN
 * @date 6/3/24 21:30 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto register(UserDto userDto) {
        User user = UserDto.toEntity(userDto);
        user = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
        User savedUser = userRepository.save(user);
        return UserDto.of(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDto findUser(Long userId) {
        User user = getUser(userId);
        return UserDto.of(user);
    }

    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = getUser(userId);
        User updateUser = User.builder()
                .userId(user.getUserId())
                .username(userDto.username() != null ? userDto.username() : user.getUsername())
                .password(userDto.password() != null ? passwordEncoder.encode(userDto.password()) : user.getPassword())
                .nickname(userDto.nickname() != null ? userDto.nickname() : user.getNickname())
                .email(userDto.email() != null ? userDto.email() : user.getEmail())
                .phone(userDto.phone() != null ? userDto.phone() : user.getPhone())
                .build();
        User savedUser = userRepository.save(updateUser);
        return UserDto.of(savedUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.delete(getUser(userId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }


}
