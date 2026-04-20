package deviate.capstone.ipss.shared;

public class UnauthorizedException extends AppException {
    public UnauthorizedException(String message) {
        super("UNAUTHORIZED", 400, message);
    }
}
