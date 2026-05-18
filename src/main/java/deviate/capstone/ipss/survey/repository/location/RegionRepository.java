package deviate.capstone.ipss.survey.repository.location;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import deviate.capstone.ipss.survey.entity.location.Region;


@Repository
public interface RegionRepository extends JpaRepository<Region, String>{
    Optional<Region> findByCode(String code);
    Optional<Region> findByRegionCode(String regionCode);
    Optional<Region> findByName(String name);
}
