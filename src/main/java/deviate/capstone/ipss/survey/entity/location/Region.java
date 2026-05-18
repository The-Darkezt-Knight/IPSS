package deviate.capstone.ipss.survey.entity.location;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Region {
    @Id
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "\"regionCode\"", insertable=false, updatable=false)
    private String regionCode;

    @OneToMany(mappedBy = "region")
    List<Province> provinces;
}
