package com.github.senocak.clinked.exception;

import com.github.senocak.clinked.dto.ExceptionDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.security.InvalidParameterException;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler({
        BadCredentialsException.class,
        ConstraintViolationException.class,
        InvalidParameterException.class,
        HttpMessageNotReadableException.class,
        MissingPathVariableException.class,
        TypeMismatchException.class,
        MissingServletRequestParameterException.class
    })
    public ResponseEntity<Object> handleBadRequestException(Exception ex) {
        return generateResponseEntity(
                HttpStatus.BAD_REQUEST,
                new String[]{ex.getMessage()});
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(RuntimeException ex) {
        return generateResponseEntity(HttpStatus.UNAUTHORIZED,
                new String[]{ex.getMessage()});
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return generateResponseEntity(HttpStatus.METHOD_NOT_ALLOWED,
                new String[]{ex.getMessage()});
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return generateResponseEntity(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                new String[]{ex.getMessage()});
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return generateResponseEntity(HttpStatus.NOT_FOUND,
                new String[]{ex.getMessage()});
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Object> handleServerException(Exception ex) {
        return generateResponseEntity(
                ((ServerException) ex).getStatusCode(),
                ((ServerException) ex).getVariables());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        return generateResponseEntity(
                HttpStatus.INTERNAL_SERVER_ERROR,
                new String[]{ex.getMessage()});
    }

    /**
     *
     * @param httpStatus -- returned code
     * @return -- returned content
     */
    private ResponseEntity<Object> generateResponseEntity(HttpStatus httpStatus, String[] variables){
        log.error("Exception is handled. HttpStatus: {}, variables: {}", httpStatus, variables);
        ExceptionDto exceptionDto = new ExceptionDto(httpStatus.value(), variables);
        return ResponseEntity.status(httpStatus).body(exceptionDto);
    }
}