package com.codenenda.codenenda.util;

public class PasswordChecker {
    // 비밀번호 유효성 검사 (예: 길이 8자 이상, 숫자 포함 등)
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return password.length() >= 8 && password.matches(".*\\d.*"); // 숫자 하나 이상 포함
    }
}

