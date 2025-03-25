package com.mhdjasir.ezLibrary.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomErrorResponse {
    private Integer httpStatus;
    private String exception;
    private String message;
}
