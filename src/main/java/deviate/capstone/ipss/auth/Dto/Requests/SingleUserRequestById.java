package deviate.capstone.ipss.auth.Dto.Requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SingleUserRequestById(
    @NotNull
    @Positive
    Long id
) {
    
}
