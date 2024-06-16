package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.FindIdDto;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
import com.minizin.travel.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("아이디 찾기 성공")
    void findId_success() {
        //given
        FindIdDto.Request request = new FindIdDto.Request();
        request.setEmail("email");
        given(userRepository.findByEmailAndLoginType(request.getEmail(), LoginType.LOCAL))
                .willReturn(Optional.of(
                        UserEntity.builder()
                                .username("username")
                                .build()
                ));


        //when
        FindIdDto.Response response = userService.findId(request);

        //then
        assertEquals("username", response.getUsername());
    }

    @Test
    @DisplayName("아이디 찾기 실패 - 등록되지 않은 이메일")
    void findId_fail_unRegisteredEmail() {
        //given
        FindIdDto.Request request = new FindIdDto.Request();
        request.setEmail("email");
        given(userRepository.findByEmailAndLoginType(request.getEmail(), LoginType.LOCAL))
                .willReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.findId(request));

        //then
        assertEquals("등록되지 않은 이메일입니다.", exception.getMessage());
    }

}