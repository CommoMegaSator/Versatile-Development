package versatile_development.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Основна сутність користувача")
public class UserDTO implements Serializable {
        @Schema(description = "Ідентифікатор")
        private Long id;

        @Size(min = 4, max = 25)
        @Schema(description = "Ім'я")
        private String firstname;

        @Size(min = 4, max = 25)
        @Schema(description = "Прізвище")
        private String lastname;

        @NotNull(message = "Field 'nickname' can not be NULL")
        @Size(min = 4, max = 25)
        @Schema(description = "Псевдонім")
        private String nickname;

        @NotNull(message = "Field 'email' can not be NULL")
        @Email
        @Size(min = 4, max = 25)
        @Schema(description = "Електронна пошта")
        private String email;

        @Schema(description = "Вік")
        private Integer age;

        @NotNull(message = "Field 'password' can not be NULL")
        @Size(min = 8, max = 25)
        @Schema(description = "Пароль")
        private String password;

        @Schema(description = "Стан акаунта")
        private boolean activated;

        @Schema(description = "Токен підтвердження")
        private String confirmationToken;

        @Schema(description = "Термін дії токена")
        private Date tokenExpiration;

        @Schema(description = "Ролі користувача")
        private Set<Role> roles;

        @Schema(description = "Дата створення акаунта")
        private Date creationDate;

        @Schema(description = "День народження")
        private Date birthday;

        @Schema(description = "Стать")
        private String gender;

        @Schema(description = "Національність")
        private String nationality;

        @Schema(description = "Про користувача")
        private String aboutUser;

        @Schema(description = "Статті цього користувача")
        private List<ArticleDTO> article;

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
