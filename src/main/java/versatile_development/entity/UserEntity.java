package versatile_development.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import versatile_development.domain.Role;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "usr")
public class UserEntity extends BaseEntity implements UserDetails {

    @Column(name = "email", length = 25, nullable = false, unique = true)
    private String email;

    @Column(name = "firstname", length = 25, nullable = false)
    private String firstname;

    @Column(name = "lastname", length = 25, nullable = false)
    private String lastname;

    @Column(name = "nickname", length = 25, nullable = false, unique = true)
    private String nickname;

    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Column(name = "age")
    private int age;

    @Column(name = "activated", columnDefinition = "boolean default false")
    private boolean activated;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "token_expiration")
    private Date tokenExpiration;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "birthday")
    private Date birthday;

    @OneToMany(mappedBy = "user")
    private List<ArticleEntity> article;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActivated();
    }
}