package deviate.capstone.ipss.survey.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import deviate.capstone.ipss.survey.entity.classification.CivilStatus;
import deviate.capstone.ipss.survey.entity.classification.MSMEClassification;
import deviate.capstone.ipss.survey.entity.location.Baranggay;
import deviate.capstone.ipss.survey.entity.location.CityMunicipality;
import deviate.capstone.ipss.survey.entity.location.Province;
import deviate.capstone.ipss.survey.entity.location.Region;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "client")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String oldClientId;
    @Column(nullable = false)
    private String clientId;
    @Column(nullable = false)
    private LocalDateTime dateCreated;
    @Column(nullable = true)
    private LocalDateTime dateUpdated;

    //Could be enums of FK references
    @Column(nullable = false)
    private String statusOfClient;     // e.g. "Potential"
    @Column(nullable = false)
    private String specifyLevel;       // from validations sheet (e.g. MSME Level)
    @Column(nullable = false)
    private String categoryOfClient;
    @Column(nullable = false)
    private String socialClassification; // from validations sheet
    @Column(nullable = false)
    private String diffAbledType;       // from validations sheet
    @Column(nullable = false)
    private Boolean isSenior;
    @Column(nullable = false)
    private Boolean isIndigeneous;
    @Column(nullable = false)
    private String levelOfDigitalization; // from validations sheet
    @Column(nullable = false)
    private String digitalTools;         // from validations sheet
    @Column(nullable = false)
    private MSMEClassification msmeClassification;   // Micro/Small/Medium/Large
    @Column(nullable = false)
    private String clientDesignation;

    //Basic information
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String middleName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String suffix;
    @Column(nullable = false)
    private CivilStatus civilStatus;   // from validations sheet
    @Column(nullable = false)
    private String sex;
    @Column(nullable = false)
    private LocalDate birthdate;
    @Column(nullable = false)
    private Integer birthYear;
    @Column(nullable = false)
    private String citizenship;

    //IDs
    @Column(nullable = false)
    private String dtiKonekId;
    @Column(nullable = false)
    private String philippineIdentificationSystem;

    //Contact
    @Column(nullable = false)
    private String landlineNumber;
    @Column(nullable = false)
    private String faxNumber;
    @Column(nullable = false)
    private String mobileNumber;
    @Column(nullable = false)
    private String emailAddress;
    @Column(nullable = false)
    private String socialMedia;
    @Column(nullable = false)
    private String website;
    @Column(nullable = false)
    private String eCommercePlatform;
    
    //Geo table - Location
    @Column(nullable = false)
    private String district;
    @Column(nullable = false)
    private String zipCode;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;

    @PrePersist
    protected void onCreate() {
        this.dateCreated = LocalDateTime.now();
        this.dateUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateUpdated = LocalDateTime.now();
    }

    @ManyToOne
    private Region region;
    @ManyToOne
    private Province province;
    @ManyToOne
    private CityMunicipality cityMunicipality;

    @ManyToOne
    @JoinColumn(name="code")
    private Baranggay baranggay;
    
}
