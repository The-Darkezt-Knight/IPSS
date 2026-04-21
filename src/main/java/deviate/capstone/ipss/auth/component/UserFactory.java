package deviate.capstone.ipss.auth.component;

import org.springframework.stereotype.Component;

import deviate.capstone.ipss.auth.Dto.Requests.RegisterRequest;
import deviate.capstone.ipss.auth.entity.Role;
import deviate.capstone.ipss.auth.entity.Sex;
import deviate.capstone.ipss.auth.entity.User;
import deviate.capstone.ipss.auth.service.PasswordService;
import deviate.capstone.ipss.shared.Utility.EnumUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final PasswordService passwordService;
    
    //Used to convert DTO into User object
    public User createGovtStaff(RegisterRequest request) {
        return User.builder()
        .firstName(request.firstName().trim())
        .lastName(request.lastName().trim())
        .middleName(request.middleName() != null ? request.middleName().trim() : null)
        .birthDate(request.birthDate())
        .baranggay(request.baranggay().trim())
        .cityMunicpality(request.cityMunicipality().trim())
        .province(request.province().trim())
        .sex(EnumUtils.parseEnum(Sex.class, request.sex(), "sex"))
        .role(EnumUtils.parseEnum(Role.class, request.role(), "role"))
        .govtEmail(request.govtEmail().trim())
        .govtId(request.govtId())
        .password(passwordService.createTemporaryPassword(request.lastName().trim(),request.govtId().toString()))
        .isActive(true)
        .build();
    }
}
