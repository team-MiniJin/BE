package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.JoinDto;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.enums.Role;
import com.minizin.travel.user.domain.enums.UserErrorCode;
import com.minizin.travel.user.domain.exception.CustomUserException;
import com.minizin.travel.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinDto.Response join(JoinDto.Request request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomUserException(UserErrorCode.USER_ALREADY_EXIST);
        }

        UserEntity saved = userRepository.save(UserEntity.builder()
                .username(request.getUsername())
                .password(
                        bCryptPasswordEncoder.encode(request.getPassword())
                )
                .nickname(request.getNickname())
                .email(request.getEmail())
                .role(Role.ROLE_USER)
                .loginType(LoginType.LOCAL)
                .build());

        return JoinDto.Response.fromUserEntity(saved);
    }
}
