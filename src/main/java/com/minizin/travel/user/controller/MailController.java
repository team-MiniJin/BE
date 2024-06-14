package com.minizin.travel.user.controller;

import com.minizin.travel.user.domain.dto.SendMailDto;
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
    public String sendMail(
            @RequestBody @Valid SendMailDto sendMailDto
    ) {

        mailService.sendMail(sendMailDto);

        return "인증코드가 발송되었습니다.";
    }
}
