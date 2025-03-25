package com.mhdjasir.ezLibrary.domain.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResetForgotPasswordDTO {
    @NotBlank
    private String newPassword;
    @NotBlank
    private String confirmPassword;
}
