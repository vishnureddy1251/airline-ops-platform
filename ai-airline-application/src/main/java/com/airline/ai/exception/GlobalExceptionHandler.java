package com.airline.ai.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, WebRequest req) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, WebRequest req) {
        return buildError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), req);
    }
    @ExceptionHandler(InsufficientSeatsException.class)
    public ResponseEntity<ErrorResponse> handleSeats(InsufficientSeatsException ex, WebRequest req) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), req);
    }
    @ExceptionHandler(InsufficientMilesException.class)
    public ResponseEntity<ErrorResponse> handleMiles(InsufficientMilesException ex, WebRequest req) {
        return buildError(HttpStatus.PAYMENT_REQUIRED, ex.getMessage(), req);
    }
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePayment(PaymentException ex, WebRequest req) {
        return buildError(HttpStatus.PAYMENT_REQUIRED, ex.getMessage(), req);
    }
    @ExceptionHandler(DuplicateBookingException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateBookingException ex, WebRequest req) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), req);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, WebRequest req) {
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(e -> {
            String field = e instanceof FieldError fe ? fe.getField() : e.getObjectName();
            errors.put(field, e.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .timestamp(LocalDateTime.now()).status(400).error("Validation Failed")
                .message("Request validation failed")
                .path(req.getDescription(false).replace("uri=",""))
                .fieldErrors(errors).build());
    }
}
