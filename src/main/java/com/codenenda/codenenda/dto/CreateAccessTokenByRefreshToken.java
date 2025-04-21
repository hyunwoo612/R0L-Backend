package com.codenenda.codenenda.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenByRefreshToken {
    private String refreshToken;
}
