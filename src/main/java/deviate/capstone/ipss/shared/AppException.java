package deviate.capstone.ipss.shared;

//base class for all exceptions
public class AppException extends RuntimeException{
    private final String code;
    private final int status;

    public AppException(String code, int status, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
