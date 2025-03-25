package com.mhdjasir.ezLibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountCredentialMailDTO {
    private String name;
    private String username;
    private String password;
}
