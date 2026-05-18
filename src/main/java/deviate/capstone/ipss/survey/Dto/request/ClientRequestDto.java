package deviate.capstone.ipss.survey.Dto.request;

import jakarta.validation.constraints.NotNull;

public record ClientRequestDto(
    @NotNull
    String clientId
) {
}
