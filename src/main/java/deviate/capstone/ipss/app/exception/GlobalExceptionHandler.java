package deviate.capstone.ipss.app.exception;

import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import deviate.capstone.ipss.shared.AppException;
import deviate.capstone.ipss.shared.ForbiddenException;
import deviate.capstone.ipss.shared.UnauthorizedException;
import deviate.capstone.ipss.shared.ValidationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
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

    //Handles all unvalidated requests
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex, WebRequest request) {
        log.warn("[VALIDATION_ERROR] path={} | message={}",
            request.getDescription(false),
            ex.getMessage()
        );

        return ResponseEntity
            .status(400)
            .body(ErrorResponse.of(400, "VALIDATION", ex.getMessage()));
    }
    
    //catches anything you didn't anticipate
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, WebRequest request) {
        log.error("[INTERNAL_ERROR] Unhandled exception | path={} | type={} | message={}",
            request.getDescription(false),
            ex.getClass().getSimpleName(),
            ex.getMessage()
        );

        return ResponseEntity
            .status(500)
            .body(ErrorResponse.of(500, "INTERNAL_ERROR", "Something went wrong"));
    }


    //User is not logged in or tokens are missing/ invalid
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        log.warn("[AUTH] Unauthorized access attempt | message={}", ex.getMessage());
        return ResponseEntity
            .status(401)
            .body(ErrorResponse.of(401, "UNAUTHORIZED", ex.getMessage()));
    }

    //Simply tells a user don't have an access
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
        log.warn("[FORBIDDEN] Forbidden access attemp | message={}", ex.getMessage());
        return ResponseEntity
            .status(403)
            .body(ErrorResponse.of(403, "FORBIDDEN", ex.getMessage()));
    }


    //Handles possible duplication of values when querying new user in the users table
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, WebRequest request) {

        log.warn("[CONFLICT] Data integrity violation | path={} | message={}",
            request.getDescription(false),
            ex.getMostSpecificCause().getCause()
        );

        String cause = ex.getMostSpecificCause().getMessage().toLowerCase();
    
        //Either this or Throwable's own message can be used
        //This time, I'm using Throwable's
        String message = "A registration conflict has occured";

        if(cause.contains("uc_users_govt_email")) {
            message = "govtEmail: Email already exists";
        } else if(cause.contains("uc_users_govt_id")) {
            message = "govtId: Employee ID already registered";
        }

        return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ErrorResponse.of(409, "CONFLICT", message));

    }
}
