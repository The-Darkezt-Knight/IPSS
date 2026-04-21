package deviate.capstone.ipss.auth.service;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder);
    }
    
    public String createTemporaryPassword(String lastName, String govtId) {
        String rawPassword = String.join(".", lastName, govtId);
        String hashedPassword = passwordEncoder.encode(rawPassword);

        return hashedPassword;
    }
}
