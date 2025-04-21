package com.codenenda.codenenda.service;

import com.codenenda.codenenda.config.JwtProperties;
import com.codenenda.codenenda.config.jwt.TokenProvider;
import com.codenenda.codenenda.domain.RefreshToken;
import com.codenenda.codenenda.domain.User;
import com.codenenda.codenenda.dto.CreateAccessTokenByRefreshToken;
import com.codenenda.codenenda.dto.CreateAccessTokenRequest;
import com.codenenda.codenenda.dto.CreateAccessTokenResponse;
import com.codenenda.codenenda.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    public CreateAccessTokenResponse getAccessToken(CreateAccessTokenRequest request) {
        User user = userService.getUserByUsername(request.getUsername());

        if(user != null) {
            if(passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return createAccessToken(user, null);
            }
        }

        return null;
    }

    private CreateAccessTokenResponse createAccessToken(User user, String refreshToken) {
        Duration tokenDuration = Duration.ofMinutes(jwtProperties.getDuration());
        Duration refreshDuration = Duration.ofSeconds(jwtProperties.getRefreshDuration());

        RefreshToken savedRefreshToken = refreshTokenRepository.findByUsername(user.getUsername()).orElse(null);

        if(savedRefreshToken != null && refreshToken != null) {
            if(!savedRefreshToken.getRefreshToken().equals(refreshToken))
                return new CreateAccessTokenResponse("Invalid token.", null, null);
        }

        String accessToken = tokenProvider.generateToken(user, tokenDuration, true);
        String newRefreshToken = tokenProvider.generateToken(user, refreshDuration, false);

        if(savedRefreshToken == null)
            savedRefreshToken = new RefreshToken(user.getUsername(), newRefreshToken);
        else
            savedRefreshToken.setRefreshToken(newRefreshToken);
        refreshTokenRepository.save(savedRefreshToken);
        return new CreateAccessTokenResponse("ok", accessToken, newRefreshToken);

    }

    public CreateAccessTokenResponse refreshAccessToken(CreateAccessTokenByRefreshToken request) {
        try {
            Claims claims = tokenProvider.getClaims(request.getRefreshToken());
            String type = claims.get("type").toString();
            System.out.println(type);
            if(type == null || !type.equals("R")) {
                throw new Exception("Invalid token");
            }

            User user = userService.getUserByUsername(claims.getSubject());
            return createAccessToken(user, request.getRefreshToken());
        } catch (ExpiredJwtException e) {
            System.out.println("Expired token");
            return new CreateAccessTokenResponse("만료된 토큰", null, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new CreateAccessTokenResponse(e.getMessage(), null, null);
        }
    }
}
