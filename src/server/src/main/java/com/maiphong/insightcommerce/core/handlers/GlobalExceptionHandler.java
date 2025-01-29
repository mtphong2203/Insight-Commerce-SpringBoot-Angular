package com.maiphong.insightcommerce.core.handlers;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.maiphong.insightcommerce.dtos.response.ResponseError;
import com.maiphong.insightcommerce.exceptions.EntityCreateUpdateException;
import com.maiphong.insightcommerce.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseError> handleIllegalArgumentException(IllegalArgumentException e) {
        var responseError = new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseError> handleResourceNotFoundException(ResourceNotFoundException e) {
        var responseError = new ResponseError(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> handleBadCredentialsException(BadCredentialsException e) {
        var responseError = new ResponseError(e.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(responseError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseError> handleAuthenticationException(AuthenticationException e) {
        var responseError = new ResponseError("Invalid credentials", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(responseError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseError> handleSQLServerException(SQLException e) {
        var responseError = new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityCreateUpdateException.class)
    public ResponseEntity<ResponseError> handleEntityCreateUpdateException(EntityCreateUpdateException e) {
        var responseError = new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
