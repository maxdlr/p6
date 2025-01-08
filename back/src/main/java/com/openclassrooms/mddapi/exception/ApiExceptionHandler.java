package com.openclassrooms.mddapi.exception;

import com.openclassrooms.mddapi.payload.response.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiResourceNotFoundException.class)
    public ResponseEntity<Object> handleApiResourceNotFoundException(
            ApiResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(
            UnauthorizedAccessException exception
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            ApiBadPostRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(
            ValidationFailureException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            ValidationFailureException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ApiBadPostRequestException.class)
    public ResponseEntity<Object> handleApiBadPostRequestException(
            ApiBadPostRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<Object> handleInvalidJwtException(InvalidJwtException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<Object> handleForbiddenAccessException(ForbiddenAccessException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(UnsupportedMediaTypeException.class)
    public ResponseEntity<Object> handleUnsupportedMediaTypeException(
            UnsupportedMediaTypeException exception) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE));
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<Object> handleMethodNotAllowedException(
            MethodNotAllowedException exception) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(PreconditionFailedException.class)
    public ResponseEntity<Object> handlePreconditionFailedException(
            PreconditionFailedException exception) {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.PRECONDITION_FAILED));
    }

    @ExceptionHandler(RequestEntityTooLargeException.class)
    public ResponseEntity<Object> handleRequestEntityTooLargeException(
            RequestEntityTooLargeException exception) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalServerErrorException(
            InternalServerErrorException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedAccessException(
            UnauthorizedAccessException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessageResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorMessageResponse(
                                "An internal error occurred. - " + exception.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
