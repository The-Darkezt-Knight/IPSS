package deviate.capstone.ipss.auth.entity;

import deviate.capstone.ipss.auth.base.BaseUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseUser{
    
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String govtEmail;

    @Column(nullable = false)
    private Long govtId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isActive;
}
