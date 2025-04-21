package com.codenenda.codenenda.service;

import com.codenenda.codenenda.domain.User;
import com.codenenda.codenenda.dto.AddUserRequest;
import com.codenenda.codenenda.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;  // 이메일 서비스 주입

    public Long save(AddUserRequest dto) {
        // 사용자 객체 생성
        User user = User.builder()
                .username(dto.getUsername())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .name(dto.getName())
                .build();

        // 사용자 저장
        user = userRepository.save(user);

        // 이메일 인증 토큰 생성
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userRepository.save(user);

        // 이메일 인증 메일 발송
        emailService.sendVerificationEmail(user);

        return user.getId();
    }

    public User updateByVerifyToken(String token) {
        User user = userRepository.findByToken(token).orElse(null);

        if (user != null) {
            user.setVerified(true);  // 인증 완료
            user.setToken(null);     // 토큰 제거
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    public boolean isVerifiedUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.isVerified();
    }

}