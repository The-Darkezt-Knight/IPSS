package deviate.capstone.ipss.survey.entity.location;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityMunicipality {
    
    @Id
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "\"provinceCode\"")
    private String provinceCode;

    @Column(nullable = false, name = "\"cityMunicipalityCode\"")
    private String cityMunicipalityCode;

    @ManyToOne
    @JoinColumn(name ="province_id")
    @JsonIgnoreProperties("cityMunicipalities")
    private Province province;

    @OneToMany(mappedBy = "cityMunicipality")
    List<Baranggay> baranggays;
    
}
