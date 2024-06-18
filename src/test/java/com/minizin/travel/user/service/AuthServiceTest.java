package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.JoinDto;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.enums.Role;
import com.minizin.travel.user.domain.enums.UserErrorCode;
import com.minizin.travel.user.domain.exception.CustomUserException;
import com.minizin.travel.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("회원가입 성공")
    void join_success() {
        //given
        JoinDto.Request request = new JoinDto.Request();
        request.setUsername("username");
        request.setPassword("password");
        request.setNickname("nickname");
        request.setEmail("email");

        given(userRepository.existsByUsername(request.getUsername()))
                .willReturn(false);
        given(bCryptPasswordEncoder.encode(request.getPassword()))
                .willReturn("password");
        given(userRepository.save(any(UserEntity.class)))
                .willReturn(UserEntity.builder()
                        .username("username")
                        .password("password")
                        .nickname("nickname")
                        .email("email")
                        .role(Role.ROLE_USER)
                        .loginType(LoginType.LOCAL)
                        .build());

        //when
        JoinDto.Response response = authService.join(request);

        //then
        assertEquals("username", response.getUsername());
        assertEquals("password", response.getPassword());
        assertEquals("nickname", response.getNickname());
        assertEquals("email", response.getEmail());
        assertEquals(Role.ROLE_USER, response.getRole());
        assertEquals(LoginType.LOCAL, response.getLoginType());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 존재하는 사용자")
    void join_fail_alreadyExistsUser() {
        //given
        JoinDto.Request request = new JoinDto.Request();
        request.setUsername("username");
        given(userRepository.existsByUsername("username"))
                .willReturn(true);

        //when
        CustomUserException customUserException = assertThrows(CustomUserException.class,
                () -> authService.join(request));

        //then
        assertEquals(UserErrorCode.USER_ALREADY_EXIST, customUserException.getUserErrorCode());
    }
}