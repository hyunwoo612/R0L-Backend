package com.codenenda.codenenda.service;

import com.codenenda.codenenda.domain.User;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${link.link}")
    private String l;


    public void sendVerificationEmail(User user) {
        try {
            String to = user.getEmail();
            String subject = "Codenenda 이메일 인증";
            String link = l + "/auth/verify?token=" + user.getToken();

            String body = "<h1>안녕하세요, Codenenda입니다.</h1>" +
                    "<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>" +
                    "<a href='" + link + "'>이메일 인증</a>";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom(new InternetAddress("potplayer93@naver.com", "Codenenda_Admin"));

            mailSender.send(message);
        } catch (Exception e) {
            log.error("이메일 전송 실패", e);
        }
    }
}

