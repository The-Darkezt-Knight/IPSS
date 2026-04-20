package deviate.capstone.ipss.auth.base;

import java.time.LocalDate;
import java.time.Period;

import deviate.capstone.ipss.auth.entity.Sex;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class BaseUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private String middleName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String baranggay;

    @Column(nullable = false)
    private String cityMunicpality;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = true)
    private LocalDate updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDate.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    public int getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }
}
