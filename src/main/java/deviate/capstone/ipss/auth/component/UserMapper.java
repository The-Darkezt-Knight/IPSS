package deviate.capstone.ipss.auth.component;

import org.springframework.stereotype.Component;

import deviate.capstone.ipss.auth.Dto.Responses.UserResponse;
import deviate.capstone.ipss.auth.entity.User;

@Component
public class UserMapper {
    
    //Used to convert User objects into DTO
    public UserResponse toDto(User user) {
        return new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getMiddleName(),
            user.getBirthDate(),
            user.getBaranggay(),
            user.getCityMunicpality(),
            user.getProvince(),
            user.getSex().toString(),
            user.getRole().toString(),
            user.getGovtEmail(),
            user.getGovtId(),
            user.isActive()
        );
    }
}
