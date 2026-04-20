package deviate.capstone.ipss.app.exception;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import deviate.capstone.ipss.shared.AppException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catches your own custom exceptions
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        return ResponseEntity
            .status(ex.getStatus())
            .body(ErrorResponse.of(ex.getStatus(), ex.getCode(), ex.getMessage()));
    }


    // Catches @Valid / @Validated failures on request bodies
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(e -> e.getField() + ":" + e.getDefaultMessage())
            .collect(Collectors.joining(", "));

        return ResponseEntity
            .status(400)
            .body(ErrorResponse.of(400,"VALIDATION_ERROR", message));
    }


    // Catches Spring Security access denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
            .status(403)
            .body(ErrorResponse.of(403, "ACCESS_DENIED", "You don't have permission to do this"));
    }

    
    //catches anything you didn't anticipate
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity
            .status(500)
            .body(ErrorResponse.of(500, "INTERNAL_ERROR", "Something went wrong"));
    }

}
