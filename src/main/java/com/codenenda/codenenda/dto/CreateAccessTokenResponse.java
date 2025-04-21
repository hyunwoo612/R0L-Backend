package com.codenenda.codenenda.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateAccessTokenResponse {
    private String result;
    private String token;
    private String refreshToken;
}
