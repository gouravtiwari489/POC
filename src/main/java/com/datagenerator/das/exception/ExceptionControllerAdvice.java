package com.datagenerator.das.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
    ErrorResponse error = new ErrorResponse();
    error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    error.setMessage("Please contact your administrator");
    error.setDeveloperMessage(
        String.format("%s: %s", ex.getClass().getSimpleName(), ex.getMessage()));
    log.error(ex.getMessage(), ex);
    return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(DataAccessResourceFailureException.class)
  public ResponseEntity<ErrorResponse> exceptionHandler(DataAccessResourceFailureException ex) {
    ErrorResponse error = new ErrorResponse();
    error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    error.setMessage("Unable to connect to database");
    error.setDeveloperMessage(
        String.format("%s: %s", ex.getClass().getSimpleName(), ex.getMessage()));
    log.error(ex.getMessage(), ex);
    return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(DependencyException.class)
  public ResponseEntity<ErrorResponse> exceptionHandler(DependencyException ex) {
    ErrorResponse error = new ErrorResponse();
    error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    error.setMessage(ex.getMessage());
    error.setDeveloperMessage(
        String.format("%s: %s", ex.getClass().getSimpleName(), ex.getMessage()));
    log.error(ex.getMessage(), ex);
    return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(AlreadyLoggedInException.class)
  public ResponseEntity<ErrorResponse> exceptionHandler(AlreadyLoggedInException ex) {
    ErrorResponse error = new ErrorResponse();
    error.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    error.setMessage(ex.getMessage());
    error.setDeveloperMessage(
        String.format("%s: %s", ex.getClass().getSimpleName(), ex.getMessage()));
    log.error(ex.getMessage(), ex);
    return new ResponseEntity<ErrorResponse>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
