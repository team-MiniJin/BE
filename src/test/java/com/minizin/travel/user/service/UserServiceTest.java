package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.*;
import com.minizin.travel.user.domain.entity.UserEntity;
import com.minizin.travel.user.domain.enums.LoginType;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailService mailService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자 정보 조회 성공")
    void getUserInfo_success() {
        //given
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("username")
                .email("email")
                .nickname("nickname")
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        given(userRepository.findByUsername(principalDetails.getUsername()))
                .willReturn(Optional.of(userEntity));

        //when
        UserDto.Response response = userService.getUserInfo(principalDetails);

        //then
        assertEquals(1L, response.getUserId());
        assertEquals("username", response.getUsername());
        assertEquals("email", response.getEmail());
        assertEquals("nickname", response.getNickname());
    }

    @Test
    @DisplayName("사용자 정보 조회 실패 - 존재하지 않는 사용자")
    void getUserInfo_fail_userNotFound() {
        //given
        UserEntity userEntity = UserEntity.builder()
                .username("username")
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        given(userRepository.findByUsername(principalDetails.getUsername()))
                .willReturn(Optional.empty());

        //when
        CustomUserException exception = assertThrows(CustomUserException.class,
                () -> userService.getUserInfo(principalDetails));

        //then
        assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getUserErrorCode());
    }

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
        CustomUserException exception = assertThrows(CustomUserException.class,
                () -> userService.findId(request));

        //then
        assertEquals(UserErrorCode.EMAIL_UN_REGISTERED, exception.getUserErrorCode());
    }

    @Test
    @DisplayName("임시 비밀번호 발급 성공")
    void findPassword_success() {
        //given
        FindPasswordDto.Request request = new FindPasswordDto.Request();
        request.setUsername("username");
        request.setEmail("email");
        UserEntity userEntity = UserEntity.builder()
                .username("username")
                .email("email")
                .build();
        given(userRepository.findByUsername(request.getUsername()))
                .willReturn(Optional.of(userEntity));
        given(mailService.sendTemporaryPassword(request))
                .willReturn("password");
        given(bCryptPasswordEncoder.encode("password"))
                .willReturn("password");

        //when
        userService.findPassword(request);

        //then
        assertEquals("password", userEntity.getPassword());
    }

    @Test
    @DisplayName("임시 비밀번호 발급 실패 - 존재하지 않는 사용자")
    void findPassword_fail_userNotFound() {
        //given
        FindPasswordDto.Request request = new FindPasswordDto.Request();
        request.setUsername("username");
        request.setEmail("email");
        given(userRepository.findByUsername(request.getUsername()))
                .willReturn(Optional.empty());

        //when
        CustomUserException exception = assertThrows(CustomUserException.class,
                () -> userService.findPassword(request));

        //then
        assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getUserErrorCode());
    }

    @Test
    @DisplayName("임시 비밀번호 발급 실패 - 사용자에게 등록된 이메일이 아님")
    void findPassword_fail_userEmailUnMatched() {
        //given
        FindPasswordDto.Request request = new FindPasswordDto.Request();
        request.setUsername("username");
        request.setEmail("email2");
        UserEntity userEntity = UserEntity.builder()
                .username("username")
                .email("email")
                .build();
        given(userRepository.findByUsername(request.getUsername()))
                .willReturn(Optional.of(userEntity));

        //when
        CustomUserException exception = assertThrows(CustomUserException.class,
                () -> userService.findPassword(request));

        //then
        assertEquals(UserErrorCode.USER_EMAIL_UN_MATCHED, exception.getUserErrorCode());
    }

    @Test
    @DisplayName("비밀번호 수정 성공")
    void updatePassword_success() {
        //given
        UpdatePasswordDto.Request request = new UpdatePasswordDto.Request();
        request.setOriginalPassword("original");
        request.setChangePassword("change");

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("username")
                .password("original")
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        given(userRepository.findByUsername(principalDetails.getUsername()))
                .willReturn(Optional.of(userEntity));
        given(bCryptPasswordEncoder.matches(request.getOriginalPassword(), userEntity.getPassword()))
                .willReturn(true);
        given(bCryptPasswordEncoder.encode(request.getChangePassword()))
                .willReturn("change");

        //when
        UpdatePasswordDto.Response response = userService.updatePassword(request, principalDetails);

        //then
        assertEquals(true, response.getSuccess());
        assertEquals(1L, response.getUserId());
        assertEquals("change", response.getChangedPassword());

    }

    @Test
    @DisplayName("비밀번호 수정 실패 - 존재하지 않는 사용자")
    void updatePassword_fail_userNotFound() {
        //given
        UpdatePasswordDto.Request request = new UpdatePasswordDto.Request();

        UserEntity userEntity = UserEntity.builder()
                .username("username")
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        given(userRepository.findByUsername(principalDetails.getUsername()))
                .willReturn(Optional.empty());

        //when
        CustomUserException exception = assertThrows(CustomUserException.class,
                () -> userService.updatePassword(request, principalDetails));

        //then
        assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getUserErrorCode());

    }

    @Test
    @DisplayName("비밀번호 수정 실패 - 일치하지 않는 비밀번호")
    void updatePassword_fail_passwordUnMatched() {
        //given
        UpdatePasswordDto.Request request = new UpdatePasswordDto.Request();
        request.setOriginalPassword("original");
        request.setChangePassword("change");

        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("username")
                .password("password")
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        given(userRepository.findByUsername(principalDetails.getUsername()))
                .willReturn(Optional.of(userEntity));
        given(bCryptPasswordEncoder.matches(request.getOriginalPassword(), userEntity.getPassword()))
                .willReturn(false);

        //when
        CustomUserException exception = assertThrows(CustomUserException.class,
                () -> userService.updatePassword(request, principalDetails));

        //then
        assertEquals(UserErrorCode.PASSWORD_UN_MATCHED, exception.getUserErrorCode());

    }

    @Test
    @DisplayName("이메일 수정 성공")
    void updateEmail_success() {
        //given
        UpdateEmailDto.Request request = new UpdateEmailDto.Request();
        request.setEmail("email2");
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("username")
                .email("email")
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        given(userRepository.findByUsername(principalDetails.getUsername()))
                .willReturn(Optional.of(userEntity));

        //when
        UpdateEmailDto.Response response = userService.updateEmail(request, principalDetails);

        //then
        assertEquals(true, response.getSuccess());
        assertEquals(1L, response.getUserId());
        assertEquals("email2", response.getChangedEmail());
    }

    @Test
    @DisplayName("이메일 수정 실패 - 존재하지 않는 사용자")
    void updateEmail_fail_userNotFound() {
        //given
        UpdateEmailDto.Request request = new UpdateEmailDto.Request();
        UserEntity userEntity = UserEntity.builder()
                .username("username")
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        given(userRepository.findByUsername(principalDetails.getUsername()))
                .willReturn(Optional.empty());

        //when
        CustomUserException exception = assertThrows(CustomUserException.class,
                () -> userService.updateEmail(request, principalDetails));

        //then
        assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getUserErrorCode());

    }

    @Test
    @DisplayName("닉네임 수정 성공")
    void updateNickname_success() {
        //given
        UpdateNicknameDto.Request request = new UpdateNicknameDto.Request();
        request.setNickname("nickname2");
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .username("username")
                .nickname("nickname")
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        given(userRepository.findByUsername(principalDetails.getUsername()))
                .willReturn(Optional.of(userEntity));

        //when
        UpdateNicknameDto.Response response = userService.updateNickname(request, principalDetails);

        //then
        assertEquals(true, response.getSuccess());
        assertEquals(1L, response.getUserId());
        assertEquals("nickname2", response.getChangedNickname());
    }

    @Test
    @DisplayName("닉네임 수정 실패 - 존재하지 않는 사용자")
    void updateNickname_fail_userNotFound() {
        //given
        UpdateNicknameDto.Request request = new UpdateNicknameDto.Request();
        UserEntity userEntity = UserEntity.builder()
                .username("username")
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        given(userRepository.findByUsername(principalDetails.getUsername()))
                .willReturn(Optional.empty());

        //when
        CustomUserException exception = assertThrows(CustomUserException.class,
                () -> userService.updateNickname(request, principalDetails));

        //then
        assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getUserErrorCode());

    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void deleteUser_success() {
        //given
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        given(userRepository.findByUsername(principalDetails.getUsername()))
                .willReturn(Optional.of(userEntity));

        //when
        DeleteUserDto.Response response = userService.deleteUser(principalDetails);

        //then
        assertEquals(true, response.getSuccess());
        assertEquals(1L, response.getUserId());
    }

    @Test
    @DisplayName("사용자 삭제 실패 - 존재하지 않는 사용자")
    void deleteUser_fail_userNotFound() {
        //given
        UserEntity userEntity = UserEntity.builder()
                .username("username")
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

        given(userRepository.findByUsername(principalDetails.getUsername()))
                .willReturn(Optional.empty());

        //when
        CustomUserException exception = assertThrows(CustomUserException.class,
                () -> userService.deleteUser(principalDetails));

        //then
        assertEquals(UserErrorCode.USER_NOT_FOUND, exception.getUserErrorCode());
    }
}