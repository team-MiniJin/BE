package com.minizin.travel.user.controller;

import com.minizin.travel.user.domain.dto.SendAuthCodeDto;
import com.minizin.travel.user.domain.dto.VerifyAuthCodeDto;
import com.minizin.travel.user.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/mails/auth-code")
    public String sendAuthCode(
            @RequestBody @Valid SendAuthCodeDto sendAuthCodeDto
    ) {
        mailService.sendAuthCode(sendAuthCodeDto);

        return "인증코드가 발송되었습니다.";
    }

    @PostMapping("/mails/auth-code/verification")
    public String verifyAuthCode(
            @RequestBody @Valid VerifyAuthCodeDto verifyAuthCodeDto
    ) {
        boolean verified = mailService.verifyAuthCode(verifyAuthCodeDto);

        return verified ? "인증 성공" : "인증 실패";
    }
}
