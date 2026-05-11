package deviate.capstone.ipss.shared;

public class ResourceNotFoundException extends AppException{
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + "NOT_FOUND", 404, resource + " WITH ID " + id + " DOES_NOT_EXISTS!");
    }

    public ResourceNotFoundException(String resource, String email) {
        super(resource + "NOT_FOUND", 404, resource + " WITH EMAIL " + email + " DOES_NOT_EXISTS!");
    }
}
