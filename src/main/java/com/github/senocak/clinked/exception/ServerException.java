package com.github.senocak.clinked.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ServerException extends Exception {
    private final String[] variables;
    private final HttpStatus statusCode;
}
