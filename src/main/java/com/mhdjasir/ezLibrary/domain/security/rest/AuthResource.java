package com.mhdjasir.ezLibrary.domain.security.rest;

import com.mhdjasir.ezLibrary.domain.security.dto.AuthResponseDTO;
import com.mhdjasir.ezLibrary.domain.security.dto.LogInDTO;
import com.mhdjasir.ezLibrary.domain.security.dto.ResetForgotPasswordDTO;
import com.mhdjasir.ezLibrary.domain.security.service.AuthService;
import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthResource {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApplicationResponseDTO> signUp(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        return ResponseEntity.ok(authService.signUp(authRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LogInDTO logInDTO) {
        return ResponseEntity.ok(authService.login(logInDTO));
    }

    @PostMapping("/token/refresh/{refresh-token}")
    public ResponseEntity<AuthResponseDTO> refreshAccessToken(@PathVariable("refresh-token") String refreshToken) {
        return ResponseEntity.ok(authService.generateRefreshToken(refreshToken));
    }

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<ApplicationResponseDTO> forgotPassword(@PathVariable("email") String email, HttpServletRequest request){
        return ResponseEntity.ok(authService.forgotPassword(email,request));
    }

    @GetMapping("/reset-password/{id}")
    public ResponseEntity<ApplicationResponseDTO> resetForgotPassword(@PathVariable("id") Long id){
        return ResponseEntity.ok(authService.resetForgotPassword(id));
    }
    @PostMapping("/reset-password/{id}")
    public ResponseEntity<ApplicationResponseDTO> resetForgotPassword(@PathVariable("id") Long id,@Valid @RequestBody ResetForgotPasswordDTO resetForgotPasswordDTO){
        return ResponseEntity.ok(authService.resetForgotPassword(id,resetForgotPasswordDTO));
    }
}
