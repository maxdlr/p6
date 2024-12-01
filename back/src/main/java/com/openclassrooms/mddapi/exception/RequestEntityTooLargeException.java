package com.openclassrooms.mddapi.exception;

public class RequestEntityTooLargeException extends RuntimeException {
  public RequestEntityTooLargeException(String message) {
    super(message);
  }
}
