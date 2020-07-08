package versatile_development.domain.dto;

import lombok.*;
import versatile_development.domain.Role;
import versatile_development.entity.ArticleEntity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

        private Long id;

        @Size(min = 4, max = 25)
        private String firstname;

        @Size(min = 4, max = 25)
        private String lastname;

        @NotNull(message = "Field 'nickname' can not be NULL")
        @Size(min = 4, max = 25)
        private String nickname;

        @NotNull(message = "Field 'email' can not be NULL")
        @Email
        @Size(min = 4, max = 25)
        private String email;

        private int age;

        @NotNull(message = "Field 'password' can not be NULL")
        @Size(min = 8, max = 25)
        private String password;

        private boolean activated;

        private String confirmationToken;

        private Date tokenExpiration;

        private Set<Role> roles;

        private Date creationDate;

        private Date birthday;

        private String gender;

        private String nationality;

        private String aboutUser;

        private List<ArticleEntity> article;

        @Override
        public String toString() {
                return "UserDTO{" +
                        "firstname='" + firstname + '\'' +
                        ", lastname='" + lastname + '\'' +
                        ", nickname='" + nickname + '\'' +
                        ", email='" + email + '\'' +
                        ", age=" + age +
                        ", password='" + password + '\'' +
                        ", activated=" + activated +
                        ", confirmationToken='" + confirmationToken + '\'' +
                        ", tokenExpiration=" + tokenExpiration +
                        ", roles=" + roles +
                        ", creationDate=" + creationDate +
                        ", birthday=" + birthday +
                        ", gender='" + gender + '\'' +
                        ", nationality='" + nationality + '\'' +
                        ", aboutUser='" + aboutUser + '\'' +
                        ", article=" + article +
                        '}';
        }
}
