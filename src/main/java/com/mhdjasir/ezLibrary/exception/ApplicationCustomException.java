package com.mhdjasir.ezLibrary.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ApplicationCustomException extends RuntimeException {
    private final HttpStatus status;
    private final String code;
    private final String message;
}
