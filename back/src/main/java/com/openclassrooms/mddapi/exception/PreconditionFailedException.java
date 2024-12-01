package com.openclassrooms.mddapi.exception;

public class PreconditionFailedException extends RuntimeException {
  public PreconditionFailedException(String message) {
    super(message);
  }
}
