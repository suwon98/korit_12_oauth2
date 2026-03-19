package com.korit12.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String email;
    private String name;
    private String role;

    // 정적 메서드 명이 of , return이 AuthResponseDto 객체를 생성하는데 객체명은 없음
    public static AuthResponseDto of (String token, String email, String name, String role) {
        return new AuthResponseDto(token, email, name, role);
    }
}
