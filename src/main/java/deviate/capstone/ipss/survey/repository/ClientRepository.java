package deviate.capstone.ipss.survey.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deviate.capstone.ipss.survey.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByBaranggayCode(Long baranggayCode);
    List<Client> findByCityMunicipalityCode(Long cityMunicipalityCode);
    List<Client> findByProvinceCode(Long provinceCode);
    List<Client> findByRegionCode(Long regionCode);

    Optional<Client> findByClientId(String id);
}
