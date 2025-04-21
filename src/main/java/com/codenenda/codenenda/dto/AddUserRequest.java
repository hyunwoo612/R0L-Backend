package com.codenenda.codenenda.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String token;
    private boolean verified;
}
