package com.codenenda.codenenda.controller;

import com.codenenda.codenenda.domain.User;
import com.codenenda.codenenda.dto.CreateAccessTokenByRefreshToken;
import com.codenenda.codenenda.dto.CreateAccessTokenRequest;
import com.codenenda.codenenda.dto.CreateAccessTokenResponse;
import com.codenenda.codenenda.service.TokenService;
import com.codenenda.codenenda.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CodenendaApiController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<CreateAccessTokenResponse> login(@RequestBody CreateAccessTokenRequest request) {
        CreateAccessTokenResponse token = tokenService.getAccessToken(request);

        User user = userService.getUserByUsername(request.getUsername());

        if (!user.isVerified()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (token != null)
            return ResponseEntity.ok().body(token);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/login/token")
    public ResponseEntity<CreateAccessTokenResponse> tokenLogin(
            @RequestBody CreateAccessTokenByRefreshToken request
    ){
        CreateAccessTokenResponse response = tokenService.refreshAccessToken(request);
        if(response != null)
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
