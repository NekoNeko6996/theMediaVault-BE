package com.hoangnam.theMediaVault.infrastructure.adapter.in.web.exception;

import com.hoangnam.theMediaVault.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @Value
    @AllArgsConstructor
    public static class DomainExeptionResponse {
        String reason;
    }
    
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<DomainExeptionResponse> handleDomainException(DomainException e) {
        return new ResponseEntity<>(new DomainExeptionResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        log.error("Runtime exception: ", e);
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleMissingRequestBody(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Thiếu dữ liệu (Body) trong request. Vui lòng kiểm tra lại cú pháp JSON hoặc form-data.");
    }
}
