package org.publications.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(DuplicateIsbnException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String,String>> handleDuplicateIsbn(DuplicateIsbnException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Duplicate ISBN"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String,String> handleBadCredentials(BadCredentialsException ex) {
        return Map.of("message","Nesprávne používateľské meno alebo heslo");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleIllegalArg(IllegalArgumentException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String,String> handleConflict(DataIntegrityViolationException ex) {
        return Map.of("message","Nickname už je obsadený");
    }
}
