package deviate.capstone.ipss.auth.Dto.Responses;

import java.time.LocalDate;

public record UserResponse(
    Long id,
    String firstName,
    String lastName,
    String middleName,
    LocalDate birthDate,
    String baranggay,
    String cityMunicipality,
    String province,
    String sex,
    String role,
    String govtEmail,
    Long govtId,
    boolean isActive
) {
    
}
