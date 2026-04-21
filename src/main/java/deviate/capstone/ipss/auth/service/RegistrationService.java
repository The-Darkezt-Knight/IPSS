package deviate.capstone.ipss.auth.service;

import java.util.Objects;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import deviate.capstone.ipss.auth.Dto.Requests.RegisterRequest;
import deviate.capstone.ipss.auth.component.UserFactory;
import deviate.capstone.ipss.auth.entity.User;
import deviate.capstone.ipss.auth.repository.UserRepository;
import deviate.capstone.ipss.shared.ValidationException;

@Service
public class RegistrationService {

    private static final Pattern GOV_PH_EMAIL = Pattern
        .compile("^[a-zA-Z0-9.\\-]+@[a-zA-Z0-9.\\-]+\\.gov+\\.ph$",
        Pattern.CASE_INSENSITIVE);
    
    private final UserRepository userRepository;
    private final UserFactory userFactory;

    public RegistrationService(UserRepository userRepository, UserFactory userFactory) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.userFactory = Objects.requireNonNull(userFactory);
    }

    public ResponseEntity<String> registerStaff(RegisterRequest request) {

        if (!GOV_PH_EMAIL.matcher(request.govtEmail().trim()).matches()) {
            throw new ValidationException("Must use a valid government email address");
        }

        if(userRepository.existsByGovtEmail(request.govtEmail().trim())) {
            throw new ValidationException("Email already exists");
        }

        if(userRepository.existsByGovtId(request.govtId())) {
            throw new ValidationException("Employee id already registered");
        }

        User newUser = userFactory.createGovtStaff(request);
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.OK)
            .body("Successfully created an account for " + request.firstName().trim().concat(" " + request.lastName().trim()));
    }
}
