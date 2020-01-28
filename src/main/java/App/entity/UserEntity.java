package App.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "usr")
public class UserEntity extends BaseEntity{

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

    @OneToMany(mappedBy = "user")
    private List<ArticleEntity> article;
}