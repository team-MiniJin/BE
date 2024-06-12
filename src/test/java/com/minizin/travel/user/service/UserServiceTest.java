package com.minizin.travel.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.minizin.travel.user.domain.User;
import com.minizin.travel.user.dto.UserDto;
import com.minizin.travel.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto("testUsername", "testPassword", "testNickname", "testEmail", "testPhone");
        user = User.builder()
                .userId(1L)
                .username("testUsername")
                .password("encodedPassword")
                .nickname("testNickname")
                .email("testEmail")
                .phone("testPhone")
                .build();

        lenient().when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");
    }

    @Test
    void registerTest(){
        //given

        when(userRepository.save(any(User.class))).thenReturn(user);


        //when
        UserDto savedUser = userService.register(userDto);

        //then
        assertThat(user.getUsername()).isEqualTo(savedUser.username());
    }

    @Test
    void findUserTest(){
        //given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        //when
        UserDto findUser = userService.findUser(1L);

        //then
        assertThat(findUser.username()).isEqualTo(user.getUsername());
        assertThat(findUser.password()).isEqualTo("encodedPassword");
        assertThat(findUser.nickname()).isEqualTo(user.getNickname());
        assertThat(findUser.email()).isEqualTo(user.getEmail());
        assertThat(findUser.phone()).isEqualTo(user.getPhone());
    }

    @Test
    public void updateUserTest() {
        //given
        User updatedUser = User.builder()
            .userId(1L)
            .username("updatedUsername")
            .password("encodedUpdatedPassword")
            .nickname("updatedNickname")
            .email("updatedEmail")
            .phone("updatedPhone")
            .build();

        UserDto updatedUserDto = new UserDto("updatedUsername", "updatedPassword", "updatedNickname", "updatedEmail", "updatedPhone");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("updatedPassword")).thenReturn("encodedUpdatedPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);


        //when
        UserDto result = userService.updateUser(1L, updatedUserDto);

        //then
        assertThat(result.username()).isEqualTo("updatedUsername");
        assertThat(result.password()).isEqualTo("encodedUpdatedPassword");
        assertThat(result.nickname()).isEqualTo("updatedNickname");
        assertThat(result.email()).isEqualTo("updatedEmail");
        assertThat(result.phone()).isEqualTo("updatedPhone");
    }

    @Test
    void deleteUserTest() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        userService.deleteUser(1L);

        // then
        verify(userRepository, times(1)).delete(user);
    }
}
