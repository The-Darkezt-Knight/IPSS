package deviate.capstone.ipss.shared;

public class ResourceNotFoundException extends AppException{
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + "NOT_FOUND", 404, resource + " WITH ID" + id + "DOES_NOT_EXISTS!");
    }
}
