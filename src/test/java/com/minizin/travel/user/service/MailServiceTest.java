package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.SendAuthCodeDto;
import com.minizin.travel.user.domain.dto.VerifyAuthCodeDto;
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
        Exception exception = assertThrows(Exception.class,
                () -> mailService.sendAuthCode(sendAuthCodeDto));

        //then
        assertEquals("인증코드 발송에 실패했습니다.", exception.getMessage());
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
}