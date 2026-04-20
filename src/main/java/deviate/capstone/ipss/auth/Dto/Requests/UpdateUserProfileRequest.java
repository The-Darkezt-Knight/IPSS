package deviate.capstone.ipss.auth.Dto.Requests;

import java.time.LocalDate;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
public record UpdateUserProfileRequest(
    @NotBlank
    String firstName,

    @NotBlank
    String lastName,

    @Nullable
    String middleName,

    @NotNull
    @Past
    LocalDate birthDate,

    @NotBlank
    String baranggay,

    @NotBlank
    String cityMunicipality,

    @NotBlank
    String province,

    @NotBlank
    String sex,

    @NotBlank
    String role,

    @NotBlank
    String govtEmail,

    @NotNull
    Long govtId
) {
}
