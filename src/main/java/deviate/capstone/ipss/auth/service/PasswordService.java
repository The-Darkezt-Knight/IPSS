package deviate.capstone.ipss.auth.service;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    
    public String createTemporaryPassword(String lastName, String govtId) {
        return String.join(".", lastName, govtId);
    }
}
