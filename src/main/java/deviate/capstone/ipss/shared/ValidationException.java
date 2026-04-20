package deviate.capstone.ipss.shared;

public class ValidationException extends AppException{
    public ValidationException(String message) {
        super("VALIDATION_ERROR", 400, message);
    }
}
