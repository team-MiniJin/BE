package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.JoinDto;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

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
        JoinDto.Request request = mock(JoinDto.Request.class);
        when(request.getUsername()).thenReturn("username");
        given(userRepository.existsByUsername(anyString()))
                .willReturn(false);
        when(request.getPassword()).thenReturn("password");
        given(bCryptPasswordEncoder.encode(anyString()))
                .willReturn("password");
        given(userRepository.save(any()))
                .willReturn(UserEntity.builder()
                        .username("username")
                        .password("password")
                        .nickname("nickname")
                        .email("local@domain.com")
                        .name("name")
                        .loginType(LoginType.LOCAL)
                        .build());

        //when
        JoinDto.Response response = authService.join(request);

        //then
        assertEquals("username", response.getUsername());
        assertEquals("password", response.getPassword());
        assertEquals("nickname", response.getNickname());
        assertEquals("local@domain.com", response.getEmail());
        assertEquals("name", response.getName());
        assertEquals(LoginType.LOCAL, response.getLoginType());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 이미 존재하는 사용자")
    void join_fail_alreadyExistsUser() {
        //given
        JoinDto.Request request = mock(JoinDto.Request.class);
        when(request.getUsername()).thenReturn("username");
        given(userRepository.existsByUsername(anyString()))
                .willReturn(true);

        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.join(request));

        //then
        assertEquals("이미 존재하는 사용자입니다.", exception.getMessage());
    }
}