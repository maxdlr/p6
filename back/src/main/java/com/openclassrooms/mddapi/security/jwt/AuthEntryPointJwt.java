package com.openclassrooms.mddapi.security.jwt;

import com.openclassrooms.mddapi.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private final Map<String, Supplier<RuntimeException>> errorCategoryMap = new HashMap<>();

  public AuthEntryPointJwt() {
    errorCategoryMap.put(
            "expired", () -> new ExpiredJwtException("JWT Token has expired. Please login again."));
    errorCategoryMap.put("invalid", () -> new InvalidJwtException("Invalid JWT Token."));
    errorCategoryMap.put(
            "forbidden", () -> new ForbiddenAccessException("Access to this resource is forbidden."));
    errorCategoryMap.put("not found", () -> new ResourceNotFoundException("Resource not found."));
    errorCategoryMap.put("conflict", () -> new ConflictException("Request conflict detected."));
    errorCategoryMap.put(
            "unsupported media", () -> new UnsupportedMediaTypeException("Unsupported media type."));
    errorCategoryMap.put(
            "method not allowed",
            () -> new MethodNotAllowedException("Method not allowed for this endpoint."));
    errorCategoryMap.put(
            "precondition failed", () -> new PreconditionFailedException("Precondition failed."));
    errorCategoryMap.put(
            "request entity too large",
            () -> new RequestEntityTooLargeException("The request entity is too large."));
    errorCategoryMap.put(
            "internal server error", () -> new InternalServerErrorException("Internal server error."));
  }

  @Override
  public void commence(
          HttpServletRequest request,
          HttpServletResponse response,
          AuthenticationException authException) throws IOException {

    // Extract the error message from the AuthenticationException
    String message = authException.getMessage();

    // Find a matching exception from the map
    String errorMessage = errorCategoryMap.entrySet().stream()
            .filter(entry -> message != null && message.toLowerCase().contains(entry.getKey()))
            .findFirst()
            .map(entry -> entry.getValue().get().getMessage()) // Extract the message from the exception
            .orElse("Unauthorized: " + message); // Default message if no match is found

    // Log the error for debugging purposes
    System.err.println("Authentication error: " + errorMessage);

    // Send the 401 response with the appropriate error message
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
  }
}
