package com.poc.gourav.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

  private int errorCode;
  private String message;
  private String developerMessage;
}
