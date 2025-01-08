package com.openclassrooms.mddapi.exception;

public class ApiBadPostRequestException extends RuntimeException {
  public ApiBadPostRequestException(String message) {
    super(message);
  }
}
