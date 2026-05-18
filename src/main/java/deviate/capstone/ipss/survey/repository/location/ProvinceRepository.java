package deviate.capstone.ipss.survey.repository.location;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deviate.capstone.ipss.survey.entity.location.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String>{
    Optional<Province> findByCode(String code);
    Optional<Province> findByProvinceCode(String provinceCode);
    List<Province> findAllByRegionCode(String code);
    Optional<Province> findByName(String name);
}
