package com.codenenda.codenenda.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenRequest {
    private String username;
    private String password;
}
