package com.openclassrooms.mddapi.exception;

public class ApiResourceNotFoundException extends RuntimeException {
  public ApiResourceNotFoundException(String message) {
    super(message);
  }
}
