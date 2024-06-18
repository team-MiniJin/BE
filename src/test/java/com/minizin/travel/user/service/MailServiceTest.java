package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.FindPasswordDto;
import com.minizin.travel.user.domain.dto.SendAuthCodeDto;
import com.minizin.travel.user.domain.dto.VerifyAuthCodeDto;
import com.minizin.travel.user.domain.enums.MailErrorCode;
import com.minizin.travel.user.domain.exception.CustomMailException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private MailService mailService;

    @Test
    @DisplayName("인증 코드 메일 발송 성공")
    void sendAuthCode_success() {
        //given
        SendAuthCodeDto sendAuthCodeDto = new SendAuthCodeDto();
        sendAuthCodeDto.setEmail("email");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        //when
        mailService.sendAuthCode(sendAuthCodeDto);

        //then
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("인증 코드 메일 발송 실패")
    void sendAuthCode_fail() {
        //given
        SendAuthCodeDto sendAuthCodeDto = new SendAuthCodeDto();
        sendAuthCodeDto.setEmail("email");

        given(javaMailSender.createMimeMessage())
                .willReturn(null);

        //when
        CustomMailException exception = assertThrows(CustomMailException.class,
                () -> mailService.sendAuthCode(sendAuthCodeDto));

        //then
        assertEquals(MailErrorCode.AUTH_CODE_SEND_FAILED, exception.getMailErrorCode());
    }

    @Test
    @DisplayName("인증 코드 일치")
    void verifyAuthCode_true() {
        //given
        VerifyAuthCodeDto verifyAuthCodeDto = new VerifyAuthCodeDto();
        verifyAuthCodeDto.setEmail("email");
        verifyAuthCodeDto.setAuthCode("authCode");

        given(redisService.getData("email"))
                .willReturn("authCode");

        //when
        Boolean verified = mailService.verifyAuthCode(verifyAuthCodeDto);

        //then
        assertEquals(true, verified);
    }

    @Test
    @DisplayName("인증 코드 불일치")
    void verifyAuthCode_false() {
        //given
        VerifyAuthCodeDto verifyAuthCodeDto = new VerifyAuthCodeDto();
        verifyAuthCodeDto.setEmail("email");
        verifyAuthCodeDto.setAuthCode("authCode");

        given(redisService.getData("email"))
                .willReturn("authCode2");

        //when
        Boolean verified = mailService.verifyAuthCode(verifyAuthCodeDto);

        //then
        assertEquals(false, verified);
    }

    @Test
    @DisplayName("임시 비밀번호 메일 발송 성공")
    void sendTemporaryPassword_success() {
        //given
        FindPasswordDto.Request request = new FindPasswordDto.Request();
        request.setEmail("email");
        request.setUsername("username");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        given(javaMailSender.createMimeMessage())
                .willReturn(mimeMessage);

        //when
        mailService.sendTemporaryPassword(request);

        //then
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("임시 비밀번호 메일 발송 실패")
    void sendTemporaryPassword_fail() {
        //given
        FindPasswordDto.Request request = new FindPasswordDto.Request();
        request.setEmail("email");
        request.setUsername("username");

        given(javaMailSender.createMimeMessage())
                .willReturn(null);

        //when
        CustomMailException exception = assertThrows(CustomMailException.class,
                () -> mailService.sendTemporaryPassword(request));

        //then
        assertEquals(MailErrorCode.TEMPORARY_PASSWORD_SEND_FAILED, exception.getMailErrorCode());
    }
}