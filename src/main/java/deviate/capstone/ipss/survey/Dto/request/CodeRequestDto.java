package deviate.capstone.ipss.survey.Dto.request;

import jakarta.validation.constraints.NotBlank;

public record CodeRequestDto(
    @NotBlank
    String code
) {
    
}
