package com.aggregator.quoteAggregator.exceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleUniqueConstraintViolation(DataIntegrityViolationException ex) {
        if (ex.getMessage().contains("Unique index") || ex.getMessage().contains("PRIMARY KEY")) {
            return new ResponseEntity<>("The quote API already exists.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Database error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
