package com.poc.gourav.controller;

import com.poc.gourav.domain.TestObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@Controller
public class BaseController {

  private String message;
  private String statusCode;
  private TestObject testObj;

  @GetMapping("/dummy")
  public ResponseEntity<?> getDummyDataForRefreshToken() {
    testObj = new TestObject("This is test");
    return new ResponseEntity<>(testObj, HttpStatus.OK);
  }
}
