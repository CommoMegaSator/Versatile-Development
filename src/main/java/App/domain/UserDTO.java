package App.domain;

import App.entity.ArticleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

        private Long id;

        @NotNull(message = "Field 'firstname' can not be NULL")
        @Size(min = 4, max = 25)
        private String firstname;

        @NotNull(message = "Field 'lastname' can not be NULL")
        @Size(min = 4, max = 25)
        private String lastname;

        @NotNull(message = "Field 'nickname' can not be NULL")
        @Size(min = 4, max = 25)
        private String nickname;

        @NotNull(message = "Field 'email' can not be NULL")
        @Email
        @Size(min = 4, max = 25)
        private String email;

        @NotNull(message = "Field 'age' can not be NULL")
        @Min(12)
        @Max(99)
        private int age;

        @NotNull(message = "Field 'password' can not be NULL")
        @Size(min = 8, max = 25)
        private String password;

        private boolean activated;

        private String confirmationToken;

        private List<ArticleEntity> article;
}
