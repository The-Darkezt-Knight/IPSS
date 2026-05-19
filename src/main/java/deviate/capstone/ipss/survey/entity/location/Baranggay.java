package deviate.capstone.ipss.survey.entity.location;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import deviate.capstone.ipss.survey.entity.Client;
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
public class Baranggay {
    
    @Id
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "\"citycode\"")
    private String cityCode;

    @Column(nullable = false, name = "\"municipalitycode\"")
    private String municipalityCode;

    @Column(nullable = false, name = "\"baranggayCode\"")
    private String baranggayCode;

    @ManyToOne
    @JoinColumn(name = "city_municipality_id")
    @JsonIgnoreProperties("baranggays")
    private CityMunicipality cityMunicipality;
    
    @OneToMany(mappedBy = "baranggay")
    List<Client> clients;
}
