package App.domain;

import App.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    public class UserEntity extends BaseEntity {

        private Long id;

        @NotNull(message = "Field 'firstname' can not be NULL")
        private String firstname;
        @NotNull(message = "Field 'lastname' can not be NULL")
        private String lastname;
        @NotNull(message = "Field 'nickname' can not be NULL")
        private String nickname;
        @NotNull(message = "Field 'password' can not be NULL")
        private String password;
        @NotNull(message = "Field 'age' can not be NULL")
        @Size(min = 2, max = 2, message = "Field 'age' should be between 12 and 99")
        private int age;
        @NotNull(message = "Field 'email' can not be NULL")
        @Email
        private String email;
    }
}
