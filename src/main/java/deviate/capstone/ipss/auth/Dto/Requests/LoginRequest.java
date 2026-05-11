package deviate.capstone.ipss.auth.Dto.Requests;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank
    String govtEmail,
    @NotBlank
    String password
) {
}
