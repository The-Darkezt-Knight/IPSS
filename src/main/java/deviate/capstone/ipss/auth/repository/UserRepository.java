package deviate.capstone.ipss.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import deviate.capstone.ipss.auth.entity.User;


public interface UserRepository extends JpaRepository<User, Long>{
    boolean existsByGovtEmail(String govtEmail);
    boolean existsByGovtId(Long id);
    Optional<User> findByGovtEmailAndGovtId(String govtEmail, Long govtId);
}
