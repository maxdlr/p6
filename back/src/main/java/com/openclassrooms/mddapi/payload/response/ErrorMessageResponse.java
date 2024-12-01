package com.openclassrooms.mddapi.payload.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ErrorMessageResponse {
  private String message;
  private HttpStatus httpStatus;

  public ErrorMessageResponse(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
