package com.mhdjasir.ezLibrary.domain.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class AuthResponseDTO {
    private HttpStatus status;
    private String code;
    private String message;
    private String accessToken;
    private String refreshToken;
//    private String role;
}