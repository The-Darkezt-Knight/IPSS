package deviate.capstone.ipss.auth.Dto.Responses;

public record LoginResponse(
    String token,
    String bearer,
    Long id,
    String role
) {
}
