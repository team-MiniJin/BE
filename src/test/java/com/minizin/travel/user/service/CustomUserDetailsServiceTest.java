package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.PrincipalDetails;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("username 으로 principalDetails 획득 성공")
    void loadUserByUsername_success() {
        //given
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.of(
                        UserEntity.builder()
                                .username("username")
                                .password("password")
                                .email("email")
                                .name("name")
                                .nickname("nickname")
                                .loginType(LoginType.LOCAL)
                                .build()
                ));

        //when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("username");

        //then
        assertEquals("username", ((PrincipalDetails) userDetails).getUserEntity().getUsername());
        assertEquals("password", ((PrincipalDetails) userDetails).getUserEntity().getPassword());
        assertEquals("email", ((PrincipalDetails) userDetails).getUserEntity().getEmail());
        assertEquals("name", ((PrincipalDetails) userDetails).getUserEntity().getName());
        assertEquals("nickname", ((PrincipalDetails) userDetails).getUserEntity().getNickname());
        assertEquals(LoginType.LOCAL, ((PrincipalDetails) userDetails).getUserEntity().getLoginType());
    }

    @Test
    @DisplayName("username 으로 principalDetails 획득 실패 - 존재하지 않는 사용자")
    void loadUserByUsername_fail_UserNotFound() {
        //given
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> customUserDetailsService.loadUserByUsername("username"));

        //then
        assertEquals("존재하지 않는 사용자입니다.", exception.getMessage());

    }
}