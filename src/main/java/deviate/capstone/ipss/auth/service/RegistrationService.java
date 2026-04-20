package deviate.capstone.ipss.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import deviate.capstone.ipss.auth.Dto.Requests.RegisterRequest;
import deviate.capstone.ipss.auth.component.UserFactory;
import deviate.capstone.ipss.auth.entity.User;
import deviate.capstone.ipss.auth.repository.UserRepository;
import jakarta.validation.ValidationException;

@Service
public class RegistrationService {
    
    private final UserRepository userRepository;
    private final UserFactory userFactory;

    public RegistrationService(UserRepository userRepository, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    public ResponseEntity<String> registerStaff(RegisterRequest request) {

        if(!request.govtEmail().trim().endsWith("gov.ph")) {
            throw new ValidationException("Must use a valid government Id");
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
