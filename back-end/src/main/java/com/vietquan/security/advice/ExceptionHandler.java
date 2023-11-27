package com.vietquan.security.advice;

import com.vietquan.security.exception.EmailAlreadyExistsException;
import com.vietquan.security.exception.InvalidPasswordException;
import com.vietquan.security.exception.MisMatchPasswordException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(
            Exception.class
    )
    public Map<String, String> handleException(Exception exception, HttpServletResponse response) throws IOException {
        Map<String, String> error = new HashMap<>();
        if (exception instanceof EmailAlreadyExistsException) {
            error.put("error", exception.getMessage());
            response.setStatus(HttpStatus.CONFLICT.value());
        } else if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validationException = (MethodArgumentNotValidException) exception;
            validationException.getBindingResult().getFieldErrors().forEach(
                    fieldError -> error.put(fieldError.getField(), fieldError.getDefaultMessage())
            );
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

        else if (exception instanceof ExpiredJwtException) {
            error.put("error", "token is expired");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        else if (exception instanceof ValidationException) {
            error.put("error",exception.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        else if (exception instanceof IllegalArgumentException) {
            error.put("error",exception.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }

        else if (exception instanceof DuplicateKeyException) {
            error.put("error", exception.getMessage());
            response.setStatus(HttpStatus.CONFLICT.value());
        } else if (exception instanceof EntityNotFoundException) {
            error.put("error", exception.getMessage());
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else if (exception instanceof AccessDeniedException) {
            error.put("error", "access denied");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else if (exception instanceof BadCredentialsException) {
            error.put("error", exception.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else if (exception instanceof SignatureException) {
            error.put("error", "invalid token string");
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
        else if (exception instanceof MalformedJwtException) {
            error.put("error", "hihi");
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
        else if (exception instanceof InvalidPasswordException) {
            error.put("error", exception.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else if (exception instanceof MisMatchPasswordException) {
            error.put("error", exception.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        } else {
            error.put("error", exception.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return error;
    }
}