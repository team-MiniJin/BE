package com.minizin.travel.user.service;

import com.minizin.travel.user.domain.dto.FindPasswordDto;
import com.minizin.travel.user.domain.dto.SendAuthCodeDto;
import com.minizin.travel.user.domain.dto.VerifyAuthCodeDto;
import com.minizin.travel.user.domain.enums.MailErrorCode;
import com.minizin.travel.user.domain.exception.CustomMailException;
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

import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    @Value("${spring.mail.username}")
    private String from;

    public Boolean verifyAuthCode(VerifyAuthCodeDto verifyAuthCodeDto) {
        String email = verifyAuthCodeDto.getEmail();
        String authCode = verifyAuthCodeDto.getAuthCode();

        String data = redisService.getData(email);
        if (data == null) {
            return false;
        }

        return authCode.equals(data);
    }

    public void sendAuthCode(SendAuthCodeDto sendAuthCodeDto) {
        try {
            javaMailSender.send(this.createEmailForm(sendAuthCodeDto.getEmail()));
        } catch (Exception e) {
            throw new CustomMailException(MailErrorCode.AUTH_CODE_SEND_FAILED);
        }

    }

    public String sendTemporaryPassword(FindPasswordDto.Request request) {
        String password = this.createPassword();
        try {
            javaMailSender.send(this.createPasswordForm(request.getEmail(), password));
        } catch (Exception e) {
            throw new CustomMailException(MailErrorCode.TEMPORARY_PASSWORD_SEND_FAILED);
        }

        return password;
    }

    private MimeMessage createPasswordForm(String email, String password) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("안녕하세요. 임시 비밀번호입니다.");
        message.setFrom(from);
        message.setText(setPasswordContext(password), "utf-8", "html");

        return message;
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

    private String createPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(8);

        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        String allChars = upper + lower + digits + special;
        for (int i = 4; i < 8; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        return password.toString();
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

    private String setPasswordContext(String password) {
        Context context = new Context();
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        context.setVariable("password", password);

        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("passwordMailForm", context);
    }
}
