package com.openclassrooms.mddapi.exception;

public class ForbiddenAccessException extends RuntimeException {
  public ForbiddenAccessException(String message) {
    super(message);
  }
}
