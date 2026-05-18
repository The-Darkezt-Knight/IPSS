package deviate.capstone.ipss.survey.repository.location;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deviate.capstone.ipss.survey.entity.location.Baranggay;

@Repository
public interface BaranggayRepository extends JpaRepository<Baranggay, String>{
    Optional<Baranggay> findByCode(String code);
    Optional<Baranggay> findByBaranggayCode(String baranggayCode);
    Optional<Baranggay> findByName(String name);
    List<Baranggay> findAllByCityMunicipalityCode(String code);
    List<Baranggay> findAllByCityCode(String code);
}
