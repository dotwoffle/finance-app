package com.dotwoffle.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

/**This class is the exception handler for exceptions thrown from the
 * {@link com.dotwoffle.api.web.ApiRestController ApiRestController}.*/
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MalformedQueryException.class)
    public ResponseEntity<Map<String, String>> handleMalformedQueryException(MalformedQueryException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error_message", e.getMessage(),
                        "param", e.PARAMETER
                ));
    }

}
