package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.SendMailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(SendMailDto sendMailDto) {
        try {
            javaMailSender.send(this.createEmailForm(sendMailDto.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException("인증코드 발송에 실패했습니다.", e);
        }

    }

    private MimeMessage createEmailForm(String email) throws MessagingException {

        String authCode = createCode();

        // redis 에 해당 인증코드 저장 (만료 시간도 함께 설정)
        redisService.saveWithExpirationTime(email, authCode, 60 * 5L);

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("안녕하세요. 인증번호입니다.");
        message.setFrom(from);
        message.setText(setContext(authCode), "utf-8", "html");

        return message;
    }

    // 인증코드 생성
    private String createCode() {
        return Integer.toString(100000 + new Random().nextInt(900000));
    }

    // 이메일 내용 초기화
    private String setContext(String authCode) {
        Context context = new Context();
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        context.setVariable("code", authCode);

        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("mailForm", context);
    }
}
