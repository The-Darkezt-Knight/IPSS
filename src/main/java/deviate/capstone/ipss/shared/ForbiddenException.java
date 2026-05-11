package deviate.capstone.ipss.shared;

public class ForbiddenException extends AppException {
    public ForbiddenException(String message) {
        super("FORBIDDEN", 403, message);
    }
}