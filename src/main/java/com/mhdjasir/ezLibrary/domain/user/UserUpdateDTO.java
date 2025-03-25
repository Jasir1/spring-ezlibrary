package com.mhdjasir.ezLibrary.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUpdateDTO {
    @NotBlank
    private String name;
    @NotBlank(message = "Mobile is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid mobile number. It should be a 10-digit number.")
    private String mobile;
    @NotBlank
    private String primaryAddress;
    private String secondaryAddress;
}
