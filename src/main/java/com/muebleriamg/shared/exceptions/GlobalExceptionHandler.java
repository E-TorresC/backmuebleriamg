package com.muebleriamg.shared.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex,
                                                         HttpServletRequest request) {
    ApiError apiError = ApiError.builder()
      .status(HttpStatus.NOT_FOUND)
      .timestamp(LocalDateTime.now())
      .message(ex.getMessage())
      .path(request.getRequestURI())
      .errors(List.of(ex.getMessage()))
      .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex,
                                                         HttpServletRequest request) {

    List<String> errors = ex.getBindingResult().getFieldErrors()
      .stream()
      .map(this::formatFieldError)
      .toList();

    ApiError apiError = ApiError.builder()
      .status(HttpStatus.BAD_REQUEST)
      .timestamp(LocalDateTime.now())
      .message("Error de validación en la solicitud.")
      .path(request.getRequestURI())
      .errors(errors)
      .build();

    return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex,
                                                            HttpServletRequest request) {

    List<String> errors = ex.getConstraintViolations()
      .stream()
      .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
      .toList();

    ApiError apiError = ApiError.builder()
      .status(HttpStatus.BAD_REQUEST)
      .timestamp(LocalDateTime.now())
      .message("Error de validación en parámetros.")
      .path(request.getRequestURI())
      .errors(errors)
      .build();

    return ResponseEntity.badRequest().body(apiError);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                               HttpServletRequest request) {

    ApiError apiError = ApiError.builder()
      .status(HttpStatus.BAD_REQUEST)
      .timestamp(LocalDateTime.now())
      .message("Cuerpo de la solicitud inválido o mal formado.")
      .path(request.getRequestURI())
      .errors(List.of(ex.getMostSpecificCause().getMessage()))
      .build();

    return ResponseEntity.badRequest().body(apiError);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGenericException(Exception ex,
                                                         HttpServletRequest request) {

    ApiError apiError = ApiError.builder()
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .timestamp(LocalDateTime.now())
      .message("Ha ocurrido un error inesperado.")
      .path(request.getRequestURI())
      .errors(List.of(ex.getMessage()))
      .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
  }

  private String formatFieldError(FieldError fieldError) {
    return fieldError.getField() + ": " + fieldError.getDefaultMessage();
  }


  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiError> handleBusinessException(BusinessException ex,
                                                          HttpServletRequest request) {

    HttpStatus status;

    // Elegimos un status según el tipo de mensaje
    switch (ex.getMessage()) {
      case "Credenciales inválidas" -> status = HttpStatus.UNAUTHORIZED; // 401
      case "Usuario bloqueado" -> status = HttpStatus.FORBIDDEN;          // 403
      case "Usuario inactivo" -> status = HttpStatus.FORBIDDEN;           // 403
      default -> status = HttpStatus.BAD_REQUEST;                         // 400
    }

    ApiError apiError = ApiError.builder()
      .status(status)
      .timestamp(LocalDateTime.now())
      .message(ex.getMessage())
      .path(request.getRequestURI())
      .errors(List.of(ex.getMessage()))
      .build();

    return ResponseEntity.status(status).body(apiError);
  }

  @ExceptionHandler({ResourceConflictException.class, DataIntegrityViolationException.class})
  public ResponseEntity<ApiError> handleConflict(Exception ex, HttpServletRequest request) {

    ApiError apiError = ApiError.builder()
      .status(HttpStatus.CONFLICT)
      .timestamp(LocalDateTime.now())
      .message("Conflicto: el recurso ya existe o viola una restricción única.")
      .path(request.getRequestURI())
      .errors(List.of(ex.getMessage()))
      .build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ApiError> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex,
                                                              HttpServletRequest request) {

    ApiError apiError = ApiError.builder()
      .status(HttpStatus.PAYLOAD_TOO_LARGE) // 413
      .timestamp(LocalDateTime.now())
      .message("El archivo excede el tamaño máximo permitido.")
      .path(request.getRequestURI())
      .errors(List.of("Reduzca el tamaño de la imagen o aumente el límite configurado en multipart."))
      .build();

    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(apiError);
  }




}
