package com.datagenerator.das.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlreadyLoggedInException extends Exception {
  private static final long serialVersionUID = -6443053415907402688L;
  private String errorMessage;
  private int statusCode;

  public AlreadyLoggedInException(String message) {
    super(message);
  }

  public AlreadyLoggedInException(String message, Exception e) {
    super(message, e);
  }

  public AlreadyLoggedInException(String errorMessage, int statusCode) {
    super(errorMessage);
    this.errorMessage = errorMessage;
    this.statusCode = statusCode;
  }
}
