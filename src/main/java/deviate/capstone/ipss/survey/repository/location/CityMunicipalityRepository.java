package deviate.capstone.ipss.survey.repository.location;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deviate.capstone.ipss.survey.entity.location.CityMunicipality;


@Repository
public interface CityMunicipalityRepository extends JpaRepository<CityMunicipality, String>{
    Optional<CityMunicipality> findByCode(String code);
    Optional<CityMunicipality> findByCityMunicipalityCode(String cityMunicipality);
    Optional<CityMunicipality> findByName(String name);
    List<CityMunicipality> findAllByProvinceCode(String code);
}
